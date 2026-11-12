import java.math.BigDecimal;

public final class ZonaUrbana implements EstrategiaZona {
    @Override
    public BigDecimal aplicarRecargo(BigDecimal tarifaBase) {
        return tarifaBase;
    }
}
