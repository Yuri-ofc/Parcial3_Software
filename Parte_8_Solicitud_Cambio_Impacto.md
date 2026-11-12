# Parte 8 · Solicitud de cambio y análisis de impacto

**Estudiante:** Geomar Jose Gonzalez Pino · **Código:** 0182310049  
**Asignatura:** Ingeniería de Software I · 2026-2 · **Rama:** `Parte8`

## 1. Solicitud y alcance

Por `(A + F) mod 3 = (3 + 9) mod 3 = 0`, la solicitud asignada es incorporar la zona insular de San Andrés, con recargo del 70 %, y no aceptar cupones. Se conservan los tramos, el recargo de fin de semana, los descuentos de cliente, el costo mínimo y el redondeo definidos en R1–R8. El cupón ausente (`null`) sí es válido.

## 2. Análisis de impacto y trazabilidad

| Elemento | Estado anterior | Cambio | Evidencia relacionada |
|---|---|---|---|
| R2 · Zona válida | Urbana, rural y nacional; insular se rechazaba. | Añadir insular como destino soportado; zonas desconocidas siguen siendo error. | CP-14 y CB-08 dejan de esperar excepción; ambos esperan $15.300. |
| R4 · Recargo por zona | Estrategias urbana, rural y nacional. | San Andrés aplica factor 1,70 sobre tarifa base. | EX-17, EX-18 y EX-19. |
| R7 · Cupón | ENVIO10 permitido en las zonas existentes; cupón desconocido se rechaza. | Para insular, cualquier cupón presente se rechaza, incluido ENVIO10; `null` continúa permitido. | EX-20 y EX-21. |
| HU-02 · Costo por peso/zona | Calcula tramos y recargos de las tres zonas. | Extiende destinos y comportamiento de recargo mediante Estrategia. | `Envios`, `EstrategiaZona`, `ZonaInsular`. |
| HU-03 · Recargos/descuentos | Descuentos de cliente y cupón según R6/R7. | En insular conserva el descuento de cliente; bloquea el cupón. | EX-18, EX-20, EX-21. |
| Código | `Envios.ZONAS` y `EstrategiaZona`. | Nueva `ZonaInsular`; política `aceptaCupon()` por estrategia; validación en coordinación. | `src/ZonaInsular.java`, `src/EstrategiaZona.java`, `src/Envios.java`. |

**Impacto fuera de alcance:** no cambian la API pública, la validación de peso/día, las otras tres zonas, las tarifas por tramo ni el modelo de datos. No existe persistencia ni configuración externa que migrar.

## 3. Clasificación del mantenimiento

Es mantenimiento **adaptativo**: EnvíaYa incorpora un destino de negocio nuevo y sus condiciones comerciales para operar en San Andrés. La validación explícita de cupones y las pruebas de regresión son medidas preventivas que acompañan la adaptación; no sustituyen la clasificación principal.

## 4. Prueba primero

Se añadieron los casos EX-17 a EX-21 y se actualizaron CP-14/CB-08 antes de tocar producción. En la primera ejecución de la suite adicional, el código anterior produjo **16/21 éxitos y 5 fallos**: EX-17/18/19 fallaron con `zona invalida`; EX-20/21 también recibieron ese error genérico, no el rechazo de cupón exigido. Así se confirmó que los casos distinguen la zona ausente y la regla del cupón.

## 5. Implementación y cálculos

`ZonaInsular` aplica `BigDecimal("1.70")`; `EstrategiaZona.aceptaCupon()` devuelve `true` por defecto y la estrategia insular lo sobrescribe a `false`. `Envios` permite la zona en el mapa y rechaza un cupón presente antes de calcular el factor del cupón. El resto del pipeline no cambia.

| ID | Entrada | Cálculo esperado | Resultado
|---|---|---|---:|
| EX-17 | 5 kg, insular, normal, día 2, sin cupón | $9.000 × 1,70 | $15.300 |
| EX-18 | 5 kg, insular, VIP, día 2, sin cupón | $9.000 × 1,70 × 0,90 = $13.770; HALF_UP al centenar | $13.800 |
| EX-19 | 5 kg, insular, normal, sábado, sin cupón | $9.000 × 1,70 + $2.000 | $17.300 |
| EX-20 | Insular con ENVIO10 | Error: cupón no permitido en zona insular | Rechazado |
| EX-21 | Insular con ENVIO20 | Error: cupón no permitido en zona insular | Rechazado |

Tras implementar, **64/64 pruebas pasan** (28 CP, 15 CB y 21 EX, incluyendo 5 casos nuevos). No hubo pruebas omitidas ni abortadas. JaCoCo reporta producción: instrucciones 292/304 (96,05 %), líneas 61/64 (95,31 %) y ramas 26/26 (100 %). El método `calcularCosto` cubre 4/4 ramas.

## 6. Versionado y configuración

La solicitud añade una capacidad compatible para los destinos previamente soportados: se publica como **`v2.1.0`**, incremento MINOR sobre `v2.0.0`. No se modifica la firma pública ni se retiran comportamientos válidos existentes. `CHANGELOG.md` registra la zona, la política de cupones y la estrategia nueva. La etiqueta se fija al commit de `Parte8`.

## 7. Evidencias

- Prueba previa fallida: `build/20261112T012217952255Z/regresion-extra/` (16/21).
- Regresión posterior: `build/20261112T012253575792Z/todos/` (64/64), con `summary.json`, `junit-console.txt`, XML JUnit y reporte JaCoCo.
- Grafo real: `git log --oneline --graph --all`, anexado al final del PDF integrado.

La evidencia describe el estado ejecutado y los resultados calculados desde la especificación; una suite finita no garantiza ausencia de defectos no cubiertos.
