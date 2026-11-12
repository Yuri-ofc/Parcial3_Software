# Notas de versión · v2.0.0

**Fecha:** 12 de noviembre de 2026  
**Tipo:** versión mayor (MAJOR).

## Resumen

Esta versión corrige los seis defectos reproducidos por los casos de prueba, separa las responsabilidades del cálculo y aplica Estrategia a las zonas. Las 59 pruebas de regresión de Parte 6 terminaron en verde; la cobertura de ramas productivas fue 22/22 (100 %).

## Cambio incompatible

Se elimina el parámetro booleano `flag` de `calc`. Las llamadas que antes usaban `calc(peso, zona, cliente, dia, cupon, flag)` deben retirar el último argumento. La presentación se realiza en el punto de entrada de demostración, fuera del cálculo.

## Migración

Antes: `calc(5, "urbana", "normal", 2, null, false)`  
Ahora: `calc(5, "urbana", "normal", 2, null)`

## Línea base

`v1.0.0` apunta al código heredado original. `v2.0.0` identifica el estado integrado posterior a la Parte 7. No se cambió ni se forzó ninguna historia compartida.
