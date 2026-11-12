import java.math.BigDecimal;

public final class ZonaRural implements EstrategiaZona {
    private static final BigDecimal FACTOR_RURAL = new BigDecimal("1.10");

    @Override
    public BigDecimal aplicarRecargo(BigDecimal tarifaBase) {
        return tarifaBase.multiply(FACTOR_RURAL);
    }
}
