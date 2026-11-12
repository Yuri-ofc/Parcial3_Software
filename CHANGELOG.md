# Changelog

Los cambios importantes de este proyecto se documentan aquí. El formato sigue [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el versionado [Semantic Versioning](https://semver.org/lang/es/).


## [2.1.0] - 2026-11-11

### Added
- Zona insular de San Andrés con recargo del 70 %, mediante una estrategia dedicada.
- Política de aceptación de cupones por estrategia; la zona insular rechaza todo cupón.
- Cinco casos de regresión de Parte 8 para recargo, fin de semana, descuento y rechazo de cupón.

### Changed
- CP-14 y CB-08 ahora esperan el costo válido de la zona insular ($15.300); se conserva el rechazo para zonas desconocidas.
- Cobertura de ramas productivas de regresión: 26/26.

## [2.0.0] - 2026-11-11

### Added
- Estrategias de zona urbana, rural y nacional.
- Suite de regresión JUnit y medición de cobertura JaCoCo.
- Validaciones explícitas de peso, zona, día y cupón.

### Changed
- Cálculo monetario con `BigDecimal` y redondeo `HALF_UP` al centenar.
- Contrato público de `calc`: se elimina el argumento `flag`.
- Se separan validaciones, tarifa, recargos, descuentos y redondeo.

### Fixed
- Límite de peso PMAX, validación de zonas y días, recargo de sábado, cupón desconocido y truncamiento monetario.

### Removed
- Impresión controlada por `flag` desde la función de cálculo.

## [1.0.0] - 2026-11-11

### Added
- Línea base heredada de EnvíaYa con parámetros personalizados y comportamiento original, incluidos los defectos documentados.
