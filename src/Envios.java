import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

/** Reglas R1-R8 del código 0182310049: descuentos acumulados y recargo previo. */
public final class Envios {
    private static final double PESO_MAXIMO_KG = 45;
    private static final BigDecimal LIMITE_TRAMO_UNO = new BigDecimal("3");
    private static final BigDecimal LIMITE_TRAMO_DOS = new BigDecimal("10");
    private static final BigDecimal TARIFA_TRAMO_UNO = new BigDecimal("6000");
    private static final BigDecimal TARIFA_TRAMO_DOS = new BigDecimal("9000");
    private static final BigDecimal COSTO_POR_KG_ADICIONAL = new BigDecimal("800");
    private static final BigDecimal RECARGO_FIN_DE_SEMANA = new BigDecimal("2000");
    private static final BigDecimal COSTO_MINIMO = new BigDecimal("5000");
    private static final BigDecimal CENTENA = new BigDecimal("100");
    private static final int PRIMER_DIA = 1;
    private static final int SABADO = 6;
    private static final int ULTIMO_DIA = 7;
    private static final String CUPON_VALIDO = "ENVIO10";
    private static final BigDecimal FACTOR_CUPON = new BigDecimal("0.90");
    private static final BigDecimal FACTOR_FRECUENTE = new BigDecimal("0.95");
    private static final BigDecimal FACTOR_VIP = new BigDecimal("0.90");
    private static final Map<String, BigDecimal> FACTORES_CLIENTE = Map.of(
        "normal", BigDecimal.ONE, "frecuente", FACTOR_FRECUENTE, "vip", FACTOR_VIP);
    private static final Map<String, EstrategiaZona> ZONAS = Map.of(
        "urbana", new ZonaUrbana(), "rural", new ZonaRural(), "nacional", new ZonaNacional(),
        "insular", new ZonaInsular());

    private Envios() { }

    public static int calcularCosto(double peso, String zona, String cliente, int dia, String cupon) {
        validarPesoYDia(peso, dia);
        EstrategiaZona estrategia = seleccionarZona(zona);
        if (cupon != null && !estrategia.aceptaCupon()) {
            throw new IllegalArgumentException("cupon no permitido en zona insular");
        }
        BigDecimal factorCupon = factorCupon(cupon);
        BigDecimal costo = estrategia.aplicarRecargo(tarifaBase(BigDecimal.valueOf(peso)));
        costo = aplicarFinDeSemana(costo, dia);
        costo = aplicarDescuentos(costo, cliente, factorCupon);
        return aplicarMinimoYRedondear(costo);
    }

    private static void validarPesoYDia(double peso, int dia) {
        if (!Double.isFinite(peso) || peso <= 0 || peso > PESO_MAXIMO_KG) {
            throw new IllegalArgumentException("peso invalido");
        }
        if (dia < PRIMER_DIA || dia > ULTIMO_DIA) {
            throw new IllegalArgumentException("dia invalido");
        }
    }

    private static EstrategiaZona seleccionarZona(String zona) {
        EstrategiaZona estrategia = ZONAS.get(Objects.requireNonNullElse(zona, ""));
        if (estrategia == null) {
            throw new IllegalArgumentException("zona invalida");
        }
        return estrategia;
    }

    private static BigDecimal factorCupon(String cupon) {
        if (cupon == null) {
            return BigDecimal.ONE;
        }
        if (!CUPON_VALIDO.equals(cupon)) {
            throw new IllegalArgumentException("cupon invalido");
        }
        return FACTOR_CUPON;
    }

    private static BigDecimal tarifaBase(BigDecimal peso) {
        if (peso.compareTo(LIMITE_TRAMO_UNO) <= 0) {
            return TARIFA_TRAMO_UNO;
        }
        if (peso.compareTo(LIMITE_TRAMO_DOS) <= 0) {
            return TARIFA_TRAMO_DOS;
        }
        return TARIFA_TRAMO_DOS.add(peso.subtract(LIMITE_TRAMO_DOS).multiply(COSTO_POR_KG_ADICIONAL));
    }

    private static BigDecimal aplicarFinDeSemana(BigDecimal costo, int dia) {
        return dia >= SABADO ? costo.add(RECARGO_FIN_DE_SEMANA) : costo;
    }

    private static BigDecimal aplicarDescuentos(BigDecimal costo, String cliente, BigDecimal factorCupon) {
        BigDecimal factorCliente = FACTORES_CLIENTE.getOrDefault(
            Objects.requireNonNullElse(cliente, "normal"), BigDecimal.ONE);
        return costo.multiply(factorCliente).multiply(factorCupon);
    }

    private static int aplicarMinimoYRedondear(BigDecimal costo) {
        return costo.max(COSTO_MINIMO).divide(CENTENA, 0, RoundingMode.HALF_UP)
            .multiply(CENTENA).intValueExact();
    }
}
