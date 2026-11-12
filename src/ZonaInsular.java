import java.math.BigDecimal;

/** Estrategia de San Andrés: recargo del 70 % y sin cupones. */
public final class ZonaInsular implements EstrategiaZona {
    private static final BigDecimal FACTOR_INSULAR = new BigDecimal("1.70");

    @Override
    public BigDecimal aplicarRecargo(BigDecimal tarifaBase) {
        return tarifaBase.multiply(FACTOR_INSULAR);
    }

    @Override
    public boolean aceptaCupon() {
        return false;
    }
}
