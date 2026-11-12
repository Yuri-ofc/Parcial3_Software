# Changelog

Los cambios importantes de este proyecto se documentan aquí. El formato sigue [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el versionado [Semantic Versioning](https://semver.org/lang/es/).

## [2.0.0] - 2026-11-12

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
