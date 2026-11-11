# Parte 3 · Diseño de casos de caja negra

**Asignatura:** Ingeniería de Software I (273365) · Grupo B1 · VI semestre
**Docente:** Ing. Robert Damián Quintero Laverde
**Periodo:** 2026-2 · Tercer corte
**Estudiante:** Geomar Jose Gonzalez Pino · **Código:** 0182310049

**Objeto de prueba:** `calc()` · código heredado `v1.0.0` (`main.java`)
**Valor de la parte:** 0,9 pts
**Documentos base:** Parte 1 (requisitos R1–R8) · Parte 2 (plan de pruebas)

---

## 1. Parámetros y reglas aplicadas

| Parámetro | Valor | Regla dependiente |
|---|---|---|
| PMAX | 45 kg | R1 |
| L1 | 3 kg (límite **incluido**, D = 0 par) | R3 |
| L2 | 10 kg (límite incluido) | R3 |
| RR | 10 % | R4 |
| RN | 40 % | R4 |
| V | 10 % | R6 |
| S | $2.000, se suma **antes** de los descuentos (E = 4 par) | R5 |
| B = 1 (impar) | cliente y cupón se **acumulan** | R6 |
| F = 9 (impar) | cupón inexistente = **error** | R7 |

**Orden de cálculo:** tarifa base (R3) → recargo de zona (R4) → recargo de fin de semana (R5) → descuento de cliente (R6) → descuento de cupón (R6) → mínimo $5.000 → redondeo a múltiplo de $100 (R8), con los $50 hacia arriba.

> Criterio de diseño: todo resultado esperado se calcula a mano desde R1–R8. Nunca se toma del comportamiento del código heredado.

---

## 2. Particiones de equivalencia

### 2.1 Peso (`peso`, kg)

| Clase | Rango / condición | Válida | Representante | Caso |
|---|---|---|---|---|
| P1 | peso ≤ 0 | No | 0 y −1 | CP-01, CP-02 |
| P2 | 0 < peso ≤ 3 (tramo 1, límite incluido) | Sí | 0,01 · 2,99 · 3 | CP-03, CP-26, CP-04 |
| P3 | 3 < peso ≤ 10 (tramo 2) | Sí | 3,01 · 9,99 · 10 | CP-05, CP-27, CP-06 |
| P4 | 10 < peso ≤ 45 (excedente) | Sí | 10,01 · 12,5 · 44,99 · 45 | CP-07, CP-08, CP-28, CP-09 |
| P5 | peso > 45 | No | 45,01 | CP-10 |

### 2.2 Zona (`zona`)

| Clase | Valor | Válida | Representante | Caso |
|---|---|---|---|---|
| Z1 | urbana (sin recargo) | Sí | urbana | CP-11 |
| Z2 | rural (+10 %) | Sí | rural | CP-12 |
| Z3 | nacional (+40 %) | Sí | nacional | CP-13 |
| Z4 | cualquier otro valor | No | insular | CP-14 |

### 2.3 Cliente (`cliente`)

| Clase | Valor | Válida | Representante | Caso |
|---|---|---|---|---|
| C1 | normal (sin descuento) | Sí | normal | CP-11 |
| C2 | frecuente (−5 %) | Sí | frecuente | CP-19 |
| C3 | vip (−10 %) | Sí | vip | CP-20 |

### 2.4 Día (`dia`)

| Clase | Rango | Válida | Representante | Caso |
|---|---|---|---|---|
| D1 | 1 a 5 (entre semana, sin recargo) | Sí | 2 | CP-11 |
| D2 | 6 y 7 (fin de semana, +$2.000) | Sí | 6 · 7 | CP-17, CP-18 |
| D3 | día < 1 | No | 0 | CP-15 |
| D4 | día > 7 | No | 8 | CP-16 |

### 2.5 Cupón (`cupon`)

| Clase | Valor | Válida | Representante | Caso |
|---|---|---|---|---|
| K1 | ausente (`null`) | Sí | — | CP-11 |
| K2 | ENVIO10 (−10 %) | Sí | ENVIO10 | CP-21 |
| K3 | cualquier otro valor | No (F impar → error) | ENVIO20 | CP-23 |

---

## 3. Análisis de valores límite

Se prueban los dos lados de cada frontera. **L1 se incluye en el tramo 1** porque D = 0 es par; por eso el valor 3 pertenece al tramo 1 y 3,01 al tramo 2.

| Frontera | Justo debajo | En la frontera | Justo encima |
|---|---|---|---|
| 0 | −1 (CP-02, error) | 0 (CP-01, error) | 0,01 (CP-03, $6.000) |
| L1 = 3 | 2,99 (CP-26, $6.000) | **3 (CP-04, $6.000: incluido)** | 3,01 (CP-05, $9.000) |
| L2 = 10 | 9,99 (CP-27, $9.000) | 10 (CP-06, $9.000: incluido) | 10,01 (CP-07, $9.008 → $9.000) |
| PMAX = 45 | 44,99 (CP-28, $36.992 → $37.000) | 45 (CP-09, $37.000) | 45,01 (CP-10, error) |

Resultado de referencia con tarifa urbana, cliente normal y día 2. La tabla muestra además que el redondeo de R8 no es truncación: 9.008 → 9.000 y 36.992 → 37.000.

---

## 4. Tabla de decisión: cliente × cupón × fin de semana

Condiciones: **C1** tipo de cliente, **C2** cupón presentado, **C3** fin de semana. Acciones: **A1** sin descuento de cliente, **A2** −5 %, **A3** −10 % cliente, **A4** −10 % cupón, **A5** +$2.000 antes de los descuentos, **A6** error.

| Regla | C1 cliente | C2 cupón | C3 fin de semana | Acciones | Caso |
|---|---|---|---|---|---|
| D1 | normal | ninguno | no | A1 | CP-11 |
| D2 | normal | ninguno | sí | A5, A1 | CP-17 |
| D3 | normal | ENVIO10 | no | A1, A4 | CP-21 |
| D4 | normal | ENVIO10 | sí | A5, A1, A4 | — |
| D5 | frecuente | ninguno | no | A2 | CP-19 |
| D6 | frecuente | ninguno | sí | A5, A2 | — |
| D7 | frecuente | ENVIO10 | no | A2, A4 | — |
| D8 | frecuente | ENVIO10 | sí | A5, A2, A4 | — |
| D9 | vip | ninguno | no | A3 | CP-20 |
| D10 | vip | ninguno | sí | A5, A3 | — |
| D11 | vip | ENVIO10 | no | A3, A4 | CP-22 |
| D12 | vip | ENVIO10 | sí | A5, A3, A4 | — |
| D13 | cualquiera | inexistente | cualquiera | A6 (error) | CP-23 |

**Factor aplicado al valor acumulado después del recargo de zona y del fin de semana**

| Cliente | Sin cupón | Con ENVIO10 |
|---|---|---|
| normal | ×1,000 | ×0,900 |
| frecuente | ×0,950 | ×0,855 |
| vip | ×0,900 | ×0,810 |

**Lectura de las reglas B y E:**

- **E par:** el recargo de fin de semana se suma **antes** del factor. En D2 el resultado es (base + 2.000) × 1,000, no base × 1,000 + 2.000 (que en este caso coincide) ni base + 2.000 después de un descuento.
- **B impar:** los dos descuentos se acumulan multiplicando, **no sumando**. Con vip + ENVIO10 el factor es 0,81 y no 0,80; sobre una base de $9.000 la diferencia es $7.290 contra $7.200.
- Aplicar primero el cliente y luego el cupón es indiferente en el resultado numérico (el producto es conmutativo), pero la especificación lo exige y descarta la suma de porcentajes.

---

## 5. Matriz de casos de prueba

Entradas en formato `peso · zona · cliente · día · cupón`. Todos los valores están en pesos.

| ID | Requisito | Entradas | Resultado esperado | Técnica |
|---|---|---|---|---|
| CP-01 | R1 | 0 kg · urbana · normal · d2 · — | Error «peso inválido» | Valor límite (0) |
| CP-02 | R1 | −1 kg · urbana · normal · d2 · — | Error «peso inválido» | Partición inválida |
| CP-03 | R1, R3 | 0,01 kg · urbana · normal · d2 · — | $6.000 | Valor límite (mínimo válido) |
| CP-04 | R1, R3 | 3 kg · urbana · normal · d2 · — | $6.000 | Valor límite L1 (incluido) |
| CP-05 | R1, R3 | 3,01 kg · urbana · normal · d2 · — | $9.000 | Valor límite L1 + ε |
| CP-06 | R1, R3 | 10 kg · urbana · normal · d2 · — | $9.000 | Valor límite L2 (incluido) |
| CP-07 | R3 | 10,01 kg · urbana · normal · d2 · — | $9.000 (9.008 → redondeo) | Valor límite L2 + ε |
| CP-08 | R3 | 12,5 kg · urbana · normal · d2 · — | $11.000 | Partición válida (proporcional) |
| CP-09 | R1, R3 | 45 kg · urbana · normal · d2 · — | $37.000 | Valor límite PMAX |
| CP-10 | R1 | 45,01 kg · urbana · normal · d2 · — | Error «peso inválido» | Valor límite PMAX + ε |
| CP-11 | R4 | 5 kg · urbana · normal · d2 · — | $9.000 | Partición válida (urbana) |
| CP-12 | R4 | 5 kg · rural · normal · d2 · — | $9.900 | Partición válida (rural) |
| CP-13 | R4 | 5 kg · nacional · normal · d2 · — | $12.600 | Partición válida (nacional) |
| CP-14 | R2 | 5 kg · insular · normal · d2 · — | Error (zona inválida) | Partición inválida |
| CP-15 | R2 | 5 kg · urbana · normal · d0 · — | Error (día inválido) | Valor límite (día 0) |
| CP-16 | R2 | 5 kg · urbana · normal · d8 · — | Error (día inválido) | Valor límite (día 8) |
| CP-17 | R5 | 5 kg · urbana · normal · d6 · — | $11.000 | Tabla de decisión D2 |
| CP-18 | R5 | 5 kg · urbana · normal · d7 · — | $11.000 | Tabla de decisión D2 |
| CP-19 | R6, R8 | 5 kg · urbana · frecuente · d2 · — | $8.600 (8.550 → redondeo) | Tabla de decisión D5 |
| CP-20 | R6 | 5 kg · urbana · vip · d2 · — | $8.100 | Tabla de decisión D9 |
| CP-21 | R6 | 5 kg · urbana · normal · d2 · ENVIO10 | $8.100 | Tabla de decisión D3 |
| CP-22 | R6, R8 | 5 kg · urbana · vip · d2 · ENVIO10 | $7.300 (7.290 → redondeo) | Tabla de decisión D11 |
| CP-23 | R7 | 5 kg · urbana · normal · d2 · ENVIO20 | Error (cupón inexistente) | Tabla de decisión D13 |
| CP-24 | R8 | 1 kg · urbana · vip · d2 · ENVIO10 | $5.000 (4.860 → mínimo) | Valor límite del mínimo |
| CP-25 | R8 | 10,0625 kg · urbana · normal · d2 · — | $9.100 (9.050 → $50 arriba) | Valor límite del redondeo |
| CP-26 | R3 | 2,99 kg · urbana · normal · d2 · — | $6.000 | Valor límite L1 − ε |
| CP-27 | R3 | 9,99 kg · urbana · normal · d2 · — | $9.000 | Valor límite L2 − ε |
| CP-28 | R3, R8 | 44,99 kg · urbana · normal · d2 · — | $37.000 (36.992 → redondeo) | Valor límite PMAX − ε |

**Total: 28 casos.** Se supera el mínimo de 20 que exige el enunciado. Los casos CP-26, CP-27 y CP-28 se añadieron al cerrar el análisis de valores límite, para cubrir el lado inferior de cada frontera; la matriz de la Parte 1 los preveía como 25 y queda actualizada en la sección 7.

---

## 6. Cálculo paso a paso

### CP-09 · 45 kg, urbana, normal, día 2 (frontera PMAX)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R1 | 0 < 45 ≤ 45 → válido | — |
| 2 | R3 | 45 > 10 → 9.000 + (45 − 10) × 800 = 9.000 + 28.000 | $37.000 |
| 3 | R4 | urbana → sin recargo | $37.000 |
| 4 | R5 | día 2 → sin recargo | $37.000 |
| 5 | R6 | cliente normal, sin cupón | $37.000 |
| 6 | R8 | 37.000 ≥ 5.000 y ya es múltiplo de 100 | **$37.000** |

### CP-17 · 5 kg, urbana, normal, día 6 (sábado)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R1 | 0 < 5 ≤ 45 → válido | — |
| 2 | R3 | 3 < 5 ≤ 10 → tramo 2 | $9.000 |
| 3 | R4 | urbana → sin recargo | $9.000 |
| 4 | R5 | sábado y E par → se suma antes de los descuentos: 9.000 + 2.000 | $11.000 |
| 5 | R6 | cliente normal, sin cupón | $11.000 |
| 6 | R8 | múltiplo de 100 | **$11.000** |

### CP-19 · 5 kg, urbana, frecuente, día 2 (descuento del 5 % y redondeo de $50)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R3 | tramo 2 | $9.000 |
| 2 | R4, R5 | urbana y día 2 → sin recargos | $9.000 |
| 3 | R6 | frecuente −5 % → 9.000 × 0,95 | $8.550 |
| 4 | R8 | 8.550 ≥ 5.000; el múltiplo de 100 más cercano es 8.600 porque los $50 suben | **$8.600** |

### CP-22 · 5 kg, urbana, vip, día 2, ENVIO10 (los dos descuentos)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R3 | tramo 2 | $9.000 |
| 2 | R4, R5 | urbana y día 2 → sin recargos | $9.000 |
| 3 | R6 | vip −10 % → 9.000 × 0,90 | $8.100 |
| 4 | R6 | cupón −10 % sobre el valor ya descontado → 8.100 × 0,90 | $7.290 |
| 5 | R8 | 7.290 ≥ 5.000; múltiplo de 100 más cercano | **$7.300** |

> Si los porcentajes se sumaran (20 %) el resultado sería $7.200: por eso el caso distingue la regla B impar.

### CP-24 · 1 kg, urbana, vip, día 2, ENVIO10 (costo mínimo)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R3 | 1 ≤ 3 → tramo 1 | $6.000 |
| 2 | R4, R5 | urbana y día 2 → sin recargos | $6.000 |
| 3 | R6 | vip −10 % → 6.000 × 0,90 | $5.400 |
| 4 | R6 | cupón −10 % → 5.400 × 0,90 | $4.860 |
| 5 | R8 | 4.860 < 5.000 → se lleva al mínimo | $5.000 |
| 6 | R8 | 5.000 ya es múltiplo de 100 | **$5.000** |

### CP-25 · 10,0625 kg, urbana, normal, día 2 (redondeo de $50 hacia arriba)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R3 | 10,0625 > 10 → 9.000 + 0,0625 × 800 = 9.000 + 50 | $9.050 |
| 2 | R4, R5, R6 | sin recargos ni descuentos | $9.050 |
| 3 | R8 | 9.050 termina en $50 exactos → sube al siguiente múltiplo de 100 | **$9.100** |

### CP-28 · 44,99 kg, urbana, normal, día 2 (proporcional y redondeo)

| Paso | Regla | Operación | Valor |
|---|---|---|---|
| 1 | R1 | 44,99 ≤ 45 → válido | — |
| 2 | R3 | 9.000 + (44,99 − 10) × 800 = 9.000 + 27.992 | $36.992 |
| 3 | R4 a R6 | sin recargos ni descuentos | $36.992 |
| 4 | R8 | 36.992 → múltiplo de 100 más cercano (369,92 → 370) | **$37.000** |

---

## 7. Cobertura y matriz de trazabilidad actualizada

| Requisito | Historias | Casos de prueba | N.º |
|---|---|---|---|
| R1 · Validación del peso | HU-01 | CP-01, CP-02, CP-03, CP-04, CP-05, CP-09, CP-10, CP-26, CP-27, CP-28 | 10 |
| R2 · Validación de zona y día | HU-01 | CP-14, CP-15, CP-16 | 3 |
| R3 · Tarifa base por tramos | HU-02 | CP-03 a CP-09, CP-26, CP-27, CP-28 | 10 |
| R4 · Recargo por zona | HU-02 | CP-11, CP-12, CP-13, CP-14 | 4 |
| R5 · Recargo de fin de semana | HU-03 | CP-17, CP-18 | 2 |
| R6 · Descuentos cliente y cupón | HU-03 | CP-19, CP-20, CP-21, CP-22 | 4 |
| R7 · Cupón inexistente | HU-03 | CP-23 | 1 |
| R8 · Costo mínimo y redondeo | HU-01 | CP-19, CP-22, CP-24, CP-25, CP-28 | 5 |

**Cobertura: 8 de 8 requisitos.** Cada requisito tiene al menos un caso, y los que manejan dinero o límites (R1, R2, R3, R8) tienen varios.

Distribución por técnica:

| Técnica | Casos | N.º |
|---|---|---|
| Particiones de equivalencia | CP-02, CP-11, CP-12, CP-13, CP-14 | 5 |
| Valores límite | CP-01, CP-03, CP-04, CP-05, CP-06, CP-07, CP-09, CP-10, CP-15, CP-16, CP-24, CP-25, CP-26, CP-27, CP-28 | 15 |
| Tabla de decisión | CP-17, CP-18, CP-19, CP-20, CP-21, CP-22, CP-23 | 7 |
| Partición válida de tramo | CP-08 | 1 |

---

## 8. Casos que discriminan el código heredado

Antes de automatizar se verificó que los casos realmente distingan el comportamiento correcto del defectuoso. Los siguientes fallan contra la versión `v1.0.0`; el reporte formal de defectos, con severidad y prioridad, corresponde a la Parte 5.

| Caso | Esperado | Obtenido en `v1.0.0` | Causa |
|---|---|---|---|
| CP-10 | Error | $37.000 | Compara contra 50 en duro y no contra PMAX = 45 |
| CP-14 | Error | $9.000 | No valida la zona (R2 ausente) |
| CP-15 | Error | $9.000 | No valida el día (R2 ausente) |
| CP-16 | Error | $11.000 | No valida el día (R2 ausente) |
| CP-17 | $11.000 | $9.000 | Usa `d > 6`: el sábado no recibe recargo |
| CP-19 | $8.600 | $8.500 | Trunca en lugar de redondear al múltiplo de 100 |
| CP-22 | $7.300 | $7.200 | Trunca 7.290 |
| CP-23 | Error | $9.000 | Ignora el cupón inexistente (R7 ausente) |
| CP-25 | $9.100 | $9.000 | Trunca 9.050 en vez de subir |
| CP-28 | $37.000 | $36.900 | Trunca 36.992 |

Los 18 casos restantes coinciden con la especificación, lo que confirma que la traducción a Java conserva la lógica del original y que la batería no está sesgada hacia el error.

---

## 9. Observaciones para la automatización (Parte 5)

1. Cada caso se implementa como una prueba independiente con nombre `test_cp_XX_descripcion`.
2. Los casos de error se verifican con aserción de excepción y mensaje, no con comparación de valores.
3. Los valores esperados se escriben como literales calculados a mano; nunca se derivan del código bajo prueba.
4. Las comparaciones monetarias son exactas porque los resultados son enteros en pesos, sin decimales.
5. Los casos CP-19, CP-22, CP-25 y CP-28 son los más sensibles al redondeo: si fallan, el defecto está en R8 y no en el cálculo base.
6. Los casos CP-17 y CP-18 separan el sábado del domingo para aislar el defecto `d > 6`.
7. Los casos de error (CP-01, CP-02, CP-10, CP-14, CP-15, CP-16, CP-23) deben fallar hoy y pasar después de la corrección de la Parte 6: son la evidencia de la prueba escrita primero.
