#!/usr/bin/env python3
"""Ejecuta JUnit y genera cobertura incluso si hay aserciones fallidas.
Uso: python3 tests/run_tests.py [--case CP-10] [--tag caja-negra|caja-blanca]
El estado de salida conserva el de JUnit: 1 indica pruebas fallidas.
"""
import argparse, hashlib, json, re, subprocess, sys, urllib.request
from datetime import datetime, timezone
from pathlib import Path
import xml.etree.ElementTree as ET
ROOT = Path(__file__).resolve().parents[1]
def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--case', type=str.upper)
    ap.add_argument('--tag', choices=['caja-negra', 'caja-blanca'])
    args = ap.parse_args()
    if args.case and not re.fullmatch(r'(CP|CB)-\d{2}', args.case):
        ap.error('ID esperado: CP-01 o CB-01')
    if args.case:
        kind, number = args.case.split('-')
        if not 1 <= int(number) <= (28 if kind == 'CP' else 15):
            ap.error('Caso inexistente: ' + args.case)
        required_tag = 'caja-negra' if kind == 'CP' else 'caja-blanca'
        if args.tag and args.tag != required_tag:
            ap.error('El ID no corresponde al grupo indicado')
        args.tag = required_tag
    cache = ROOT / '.tools'; cache.mkdir(exist_ok=True)
    lock = json.loads((ROOT/'tests/dependencies.lock.json').read_text())
    for name, item in lock.items():
        path = cache/name
        if not path.exists():
            data = urllib.request.urlopen(item['url'], timeout=60).read()
            if hashlib.sha256(data).hexdigest() != item['sha256']:
                raise RuntimeError('SHA-256 incorrecto: ' + name)
            path.write_bytes(data)
        if hashlib.sha256(path.read_bytes()).hexdigest() != item['sha256']:
            raise RuntimeError('Dependencia local alterada: ' + name)
    label = args.case or args.tag or 'todos'
    out = ROOT/'build'/datetime.now(timezone.utc).strftime('%Y%m%dT%H%M%S%fZ')/label
    prod = out/'classes'; prod.mkdir(parents=True)
    test = out/'test-classes'; test.mkdir()
    junit = cache/'junit-platform-console-standalone-1.11.4.jar'
    subprocess.run(['javac','-g','-encoding','UTF-8','-d',str(prod),str(ROOT/'main.java')],check=True)
    subprocess.run(['javac','-g','-encoding','UTF-8','-cp',str(junit)+':'+str(prod),
        '-d',str(test),str(ROOT/'tests/EnviosTest.java')],check=True)
    cmd = ['java','-javaagent:'+str(cache/'jacocoagent-0.8.13.jar')+'=destfile='+str(out/'jacoco.exec')+',includes=main',
        '-jar',str(junit),'execute','--class-path',str(prod)+':'+str(test),
        '--select-class','EnviosTest','--details','tree','--disable-ansi-colors',
        '--reports-dir',str(out/'junit-xml')]
    if args.tag: cmd += ['--include-tag',args.tag]
    if args.case:
        cmd.insert(1, '-Denvios.case='+args.case)
    result = subprocess.run(cmd,cwd=ROOT,text=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
    (out/'junit-console.txt').write_text(result.stdout)
    print(result.stdout)
    if result.returncode not in (0,1):
        raise RuntimeError('JUnit no completó la ejecución: '+str(result.returncode))
    subprocess.run(['java','-jar',str(cache/'jacococli-0.8.13.jar'),'report',str(out/'jacoco.exec'),
        '--classfiles',str(prod),'--sourcefiles',str(ROOT),'--html',str(out/'coverage'),
        '--xml',str(out/'jacoco.xml'),'--csv',str(out/'jacoco.csv'),
        '--name','EnvíaYa · código heredado · '+label],check=True)
    root = ET.parse(out/'jacoco.xml').getroot()
    cl = root.find('./package/class[@name="main"]')
    method = cl.find('./method[@name="calc"][@desc="(DLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Z)I"]')
    counters = {el.attrib['type']:{'missed':int(el.attrib['missed']),'covered':int(el.attrib['covered'])} for el in method.findall('counter')}
    results=[]
    for xml in sorted((out/'junit-xml').glob('TEST-*.xml')):
        for el in ET.parse(xml).getroot().iter('testcase'):
            # En el XML legacy, el ID se encuentra en system-out (display-name).
            text = ' '.join([el.attrib.get('name','')] + [x.text or '' for x in el.findall('system-out')])
            match = re.search(r'\b(?:CP|CB)-\d{2}\b',text)
            if not match: continue
            failure = el.find('failure')
            error = el.find('error')
            failed = failure is not None or error is not None
            results.append({'id':match.group(0),'passed':not failed,'message':failure.attrib.get('message','') if failure is not None else (error.attrib.get('message','') if error is not None else '')})
    results.sort(key=lambda c:c['id'])
    if not results: raise RuntimeError('No se identificaron casos ejecutados en JUnit XML')
    source = (ROOT/'main.java').read_bytes()
    summary = {'output':str(out.relative_to(ROOT)),'junit_exit':result.returncode,
        'source_sha256':hashlib.sha256(source).hexdigest(),'tests':results,'method_counters':counters,
        'class_counters':{x.attrib['type']:{'missed':int(x.attrib['missed']),'covered':int(x.attrib['covered'])} for x in cl.findall('counter')}}
    (out/'summary.json').write_text(json.dumps(summary,indent=2,ensure_ascii=False))
    print('RESULTADOS: '+str(out));print('Resumen: '+str(sum(c['passed'] for c in results))+'/'+str(len(results))+' casos pasan')
    print('Cobertura calc: '+json.dumps(counters))
    return result.returncode
if __name__ == '__main__': sys.exit(main())
