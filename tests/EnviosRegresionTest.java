import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("regresion-extra")
public class EnviosRegresionTest {
    static Stream<Arguments> combinaciones() {
        return Stream.of(
            Arguments.of("EX-01", 4.0, "rural", "vip", 6, "ENVIO10", 9600),
            Arguments.of("EX-02", 5.0, "urbana", "frecuente", 2, "ENVIO10", 7700),
            Arguments.of("EX-03", 5.0, "urbana", "frecuente", 7, "ENVIO10", 9400),
            Arguments.of("EX-04", 5.0, "urbana", "normal", 6, "ENVIO10", 9900),
            Arguments.of("EX-05", 5.0, "urbana", "vip", 6, null, 9900),
            Arguments.of("EX-06", 12.5, "nacional", "normal", 2, null, 15400),
            Arguments.of("EX-07", 10.06125, "urbana", "normal", 2, null, 9000),
            Arguments.of("EX-08", 10.06375, "urbana", "normal", 2, null, 9100),
            Arguments.of("EX-15", Double.MIN_VALUE, "urbana", "normal", 2, null, 6000),
            Arguments.of("EX-16", 45.0, "nacional", "normal", 2, null, 51800),
            Arguments.of("EX-17", 5.0, "insular", "normal", 2, null, 15300),
            Arguments.of("EX-18", 5.0, "insular", "vip", 2, null, 13800),
            Arguments.of("EX-19", 5.0, "insular", "normal", 6, null, 17300)
        );
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("combinaciones")
    void combinacionesYPrecision(String id, double peso, String zona, String cliente,
                                int dia, String cupon, int esperado) {
        assertEquals(esperado, main.calc(peso, zona, cliente, dia, cupon), id);
    }
    static Stream<Arguments> pesosNoFinitos() {
        return Stream.of(Arguments.of("EX-09", Double.NaN),
            Arguments.of("EX-10", Double.POSITIVE_INFINITY),
            Arguments.of("EX-11", Double.NEGATIVE_INFINITY));
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("pesosNoFinitos")
    void rechazaPesosNoFinitos(String id, double peso) {
        assertThrows(IllegalArgumentException.class,
            () -> main.calc(peso, "urbana", "normal", 2, null), id);
    }
    @Test
    @DisplayName("EX-12")
    void rechazaZonaNula() {
        assertThrows(IllegalArgumentException.class,
            () -> main.calc(5, null, "normal", 2, null));
    }
    @Test
    @DisplayName("EX-13")
    void cuponOpcional() {
        assertEquals(9000, main.calc(5, "urbana", "normal", 2));
    }
    static Stream<Arguments> cuponesInsulares() {
        return Stream.of(Arguments.of("EX-20", "ENVIO10"), Arguments.of("EX-21", "ENVIO20"));
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("cuponesInsulares")
    void rechazaCualquierCuponEnZonaInsular(String id, String cupon) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> main.calc(5, "insular", "normal", 2, cupon), id);
        assertTrue(error.getMessage().toLowerCase().contains("cupon"),
            id + ": el motivo debe ser la política de cupones insulares");
    }

    @Test
    @DisplayName("EX-14")
    void apiSinFlag() {
        assertThrows(NoSuchMethodException.class,
            () -> main.class.getDeclaredMethod("calc", double.class, String.class,
                String.class, int.class, String.class, boolean.class));
    }
}
