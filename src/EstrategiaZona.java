import java.math.BigDecimal;

/** Estrategia que incorpora el recargo propio de un destino. */
public interface EstrategiaZona {
    BigDecimal aplicarRecargo(BigDecimal tarifaBase);

    default boolean aceptaCupon() {
        return true;
    }
}
