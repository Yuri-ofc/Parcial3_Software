# Parte 7 · Gestión de configuración

**Estudiante:** Geomar Jose Gonzalez Pino · **Código:** 0182310049  
**Asignatura:** Ingeniería de Software I · Grupo B1 · 2026-2  
**Rama de entrega:** `Parte7` · **Commit:** `Gestión de configuración`

## Línea base y etiquetado

Se identifica como versión heredada `v1.0.0` el commit `964757ed02b4efc1859d5e7d0bebef0c11996e83` (11 de noviembre de 2026, 17:48, “Requisitos y criterios de aceptación”). Ese árbol contiene `main.java` con la función original y sus defectos, antes de las correcciones. La etiqueta anotada permite recuperar esa línea base sin alterar el historial.

La entrega de mantenimiento se publica como `v2.0.0`, sobre el commit de Parte 7. Se corrigieron defectos y se eliminó el argumento público `flag` de `calc`. Aunque los resultados corregidos son compatibles con R1–R8, retirar un argumento cambia la interfaz y puede romper a quienes llamen la firma anterior; SemVer exige incrementar MAJOR. El `MINOR` y `PATCH` reinician a cero en la nueva línea mayor.

## Ramas e integración

La rama `Parte7` parte del estado integrado de `rama6` (`d67e070`, “Mantenimiento y refactorización”). La inspección de todas las referencias existentes muestra ramas por entregable (`main`, `parte3`, `Parte4`, `parte5`, `rama6`), pero no referencias `fix/DEF-*`, `refactor/estrategia` ni `test/suite`. Las seis correcciones están concentradas en los commits de los entregables anteriores; el historial preservado no permite demostrar merges individuales por defecto. No se crean ramas vacías ni commits retroactivos para aparentar ese flujo. La evidencia de `git log --oneline --graph --all` se adjunta en el PDF integrado.

| Referencia | Estado comprobado | Evidencia/alcance |
|---|---|---|
| `fix/DEF-01` a `fix/DEF-06` | No existen como ramas en el historial recibido | Las seis causas están corregidas y probadas en Parte 6; no hay merges individuales verificables. |
| `refactor/estrategia` | No existe como rama | El patrón Estrategia está integrado en `rama6`. |
| `test/suite` | No existe como rama | La suite JUnit/JaCoCo está en el estado integrado de Parte 5 y Parte 6. |
| `Parte7` | Creada para esta entrega | Incluye registro de versiones, changelog y notas. |

Este registro distingue la evidencia real de una reconstrucción hipotética. Una futura iteración puede migrar cada cambio a ramas temáticas con PR/merge verificable, pero eso requeriría reconstruir los cambios y no forma parte del historial recibido.

## Versiones publicadas

| Versión | Línea base | Contenido | Justificación |
|---|---|---|---|
| `v1.0.0` | `964757e` | Código heredado original. | Línea base inicial identificada y etiquetada. |
| `v2.0.0` | commit de `Parte7` | Correcciones, refactorización, estrategias, suite y retiro de `flag`. | Cambio incompatible en la firma pública; incremento MAJOR. |

## Distribución temporal de commits

Los seis commits preexistentes de `main` a `rama6` tienen marcas de autor entre 17:48 y 20:03 del 11/11/2026, con intervalos entre entregas. Se conserva esa historia; no se modifican fechas ni se reescriben commits. La evidencia reproducible completa, incluyendo la rama actual y etiquetas, aparece en el grafo Git anexado al PDF.

## Archivos de versión

`CHANGELOG.md` sigue Keep a Changelog: encabezados `Added`, `Changed`, `Fixed` y `Removed`, organizados por versión. `NOTAS_v2.0.0.md` resume el cambio, compatibilidad, migración de llamadas y pruebas.
