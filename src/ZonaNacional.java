import java.math.BigDecimal;

public final class ZonaNacional implements EstrategiaZona {
    private static final BigDecimal FACTOR_NACIONAL = new BigDecimal("1.40");

    @Override
    public BigDecimal aplicarRecargo(BigDecimal tarifaBase) {
        return tarifaBase.multiply(FACTOR_NACIONAL);
    }
}
