import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Oraculos literales de R1-R8; nunca se calculan con el codigo bajo prueba. */
public class EnviosTest {
    static Stream<Arguments> cajaNegra() {
        return seleccionar(Stream.of(
            Arguments.of("CP-01", 0, "urbana", "normal", 2, null, false, null, "peso invalido"),
            Arguments.of("CP-02", -1, "urbana", "normal", 2, null, false, null, "peso invalido"),
            Arguments.of("CP-03", 0.01, "urbana", "normal", 2, null, false, 6000, null),
            Arguments.of("CP-04", 3, "urbana", "normal", 2, null, false, 6000, null),
            Arguments.of("CP-05", 3.01, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CP-06", 10, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CP-07", 10.01, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CP-08", 12.5, "urbana", "normal", 2, null, false, 11000, null),
            Arguments.of("CP-09", 45, "urbana", "normal", 2, null, false, 37000, null),
            Arguments.of("CP-10", 45.01, "urbana", "normal", 2, null, false, null, "peso invalido"),
            Arguments.of("CP-11", 5, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CP-12", 5, "rural", "normal", 2, null, false, 9900, null),
            Arguments.of("CP-13", 5, "nacional", "normal", 2, null, false, 12600, null),
            Arguments.of("CP-14", 5, "insular", "normal", 2, null, false, 15300, null),
            Arguments.of("CP-15", 5, "urbana", "normal", 0, null, false, null, "error"),
            Arguments.of("CP-16", 5, "urbana", "normal", 8, null, false, null, "error"),
            Arguments.of("CP-17", 5, "urbana", "normal", 6, null, false, 11000, null),
            Arguments.of("CP-18", 5, "urbana", "normal", 7, null, false, 11000, null),
            Arguments.of("CP-19", 5, "urbana", "frecuente", 2, null, false, 8600, null),
            Arguments.of("CP-20", 5, "urbana", "vip", 2, null, false, 8100, null),
            Arguments.of("CP-21", 5, "urbana", "normal", 2, "ENVIO10", false, 8100, null),
            Arguments.of("CP-22", 5, "urbana", "vip", 2, "ENVIO10", false, 7300, null),
            Arguments.of("CP-23", 5, "urbana", "normal", 2, "ENVIO20", false, null, "error"),
            Arguments.of("CP-24", 1, "urbana", "vip", 2, "ENVIO10", false, 5000, null),
            Arguments.of("CP-25", 10.0625, "urbana", "normal", 2, null, false, 9100, null),
            Arguments.of("CP-26", 2.99, "urbana", "normal", 2, null, false, 6000, null),
            Arguments.of("CP-27", 9.99, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CP-28", 44.99, "urbana", "normal", 2, null, false, 37000, null)
        ));
    }
    static Stream<Arguments> cajaBlanca() {
        return seleccionar(Stream.of(
            Arguments.of("CB-01", 5, "urbana", "normal", 2, null, false, 9000, null),
            Arguments.of("CB-02", 0, "urbana", "normal", 2, null, false, null, "peso invalido"),
            Arguments.of("CB-03", 51, "urbana", "normal", 2, null, false, null, "peso invalido"),
            Arguments.of("CB-04", 1, "urbana", "normal", 2, null, false, 6000, null),
            Arguments.of("CB-05", 12.5, "urbana", "normal", 2, null, false, 11000, null),
            Arguments.of("CB-06", 5, "rural", "normal", 2, null, false, 9900, null),
            Arguments.of("CB-07", 5, "nacional", "normal", 2, null, false, 12600, null),
            Arguments.of("CB-08", 5, "insular", "normal", 2, null, false, 15300, null),
            Arguments.of("CB-09", 5, "urbana", "normal", 7, null, false, 11000, null),
            Arguments.of("CB-10", 5, "urbana", "frecuente", 2, null, false, 8600, null),
            Arguments.of("CB-11", 5, "urbana", "vip", 2, null, false, 8100, null),
            Arguments.of("CB-12", 5, "urbana", "normal", 2, "ENVIO10", false, 8100, null),
            Arguments.of("CB-13", 5, "urbana", "normal", 2, "ENVIO20", false, null, "error"),
            Arguments.of("CB-14", 1, "urbana", "vip", 2, "ENVIO10", false, 5000, null),
            Arguments.of("CB-15", 5, "urbana", "normal", 2, null, true, 9000, null)
        ));
    }
    private static Stream<Arguments> seleccionar(Stream<Arguments> casos) {
        String id = System.getProperty("envios.case");
        return id == null ? casos : casos.filter(a -> id.equals(a.get()[0]));
    }
    @Tag("caja-negra")
    @ParameterizedTest(name = "{0}")
    @MethodSource("cajaNegra")
    void contratoCajaNegra(String id, double peso, String zona, String cliente,
                          int dia, String cupon, boolean flag, Integer esperado, String error) {
        verificar(id, peso, zona, cliente, dia, cupon, flag, esperado, error);
    }
    @Tag("caja-blanca")
    @ParameterizedTest(name = "{0}")
    @MethodSource("cajaBlanca")
    void contratoCajaBlanca(String id, double peso, String zona, String cliente,
                           int dia, String cupon, boolean flag, Integer esperado, String error) {
        verificar(id, peso, zona, cliente, dia, cupon, flag, esperado, error);
    }
    private void verificar(String id, double p, String z, String c, int d,
                           String cup, boolean flag, Integer esperado, String error) {
        if (error != null) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> main.calc(p, z, c, d, cup), id + ": debe rechazar los datos");
            // R2 y R7 no fijan mensaje; R1 se compara ignorando solo tildes.
            if (!error.equals("error")) {
                String mensaje = Normalizer.normalize(ex.getMessage(), Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");
                assertEquals(error, mensaje, id + ": mensaje de peso");
            }
        } else if (flag) {
            PrintStream anterior = System.out;
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            int obtenido;
            try (PrintStream capturada = new PrintStream(salida, true, StandardCharsets.UTF_8)) {
                System.setOut(capturada);
                obtenido = main.calc(p, z, c, d, cup);
            } finally {
                System.setOut(anterior);
            }
            assertEquals(esperado.intValue(), obtenido, id);
            assertEquals("",
                salida.toString(StandardCharsets.UTF_8), id + ": el calculo no imprime");
        } else {
            assertEquals(esperado.intValue(), main.calc(p, z, c, d, cup), id);
        }
    }
}
