# Notas de versión · v2.1.0

**Fecha:** 11 de noviembre de 2026  
**Tipo:** versión menor (MINOR).

## Novedades
- Zona insular de San Andrés con recargo del 70 %.
- Los envíos insulares rechazan cualquier cupón; el cupón ausente está permitido.
- Descuento de cliente, recargo de fin de semana, mínimo y redondeo conservan las reglas existentes.

## Compatibilidad
No cambia la firma pública ni el comportamiento de las zonas urbana, rural y nacional. Se amplía el conjunto de valores admitidos para `zona` con `insular`.

## Verificación
64/64 pruebas pasan, sin omisiones ni abortos. JaCoCo: cobertura global de ramas 26/26 (100 %).
