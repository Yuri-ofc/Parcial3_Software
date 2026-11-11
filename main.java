import java.util.HashMap;
import java.util.Map;

/**
 * EnviaYa - Calculo del costo de un envio (version heredada 1.0.0).
 *
 * Traduccion fiel de envios.py (Parcial 3 - Ingenieria de Software).
 * La logica se conserva TAL CUAL, defectos incluidos, como pide el enunciado.
 * No se corrige nada aqui: las correcciones van en la Parte 6.
 *
 * Antes de empezar: llena CONFIG con tus valores (Parte 1 del parcial).
 */
public class main {

    /**
     * Parametros del codigo estudiantil 0182310049 (A=3 B=1 C=0 D=0 E=4 F=9):
     *   PMAX = 45 kg   L1 = 3 kg   L2 = 10 kg
     *   RR = 10 %      RN = 40 %   V = 10 %    S = $2.000
     *
     * CONFIG = {
     *   "L1": 3,
     *   "L2": 10,
     *   "RECARGO_RURAL": 0.10,
     *   "RECARGO_NACIONAL": 0.40,
     *   "DESC_VIP": 0.10,
     *   "RECARGO_FINDE": 2000,
     * }
     */
    static final Map<String, Double> CONFIG = new HashMap<>();

    static {
        CONFIG.put("L1", 3.0);               // 2 + (B mod 3)         = 2 + 1 = 3
        CONFIG.put("L2", 10.0);              // 10 + C                = 10 + 0 = 10
        CONFIG.put("RECARGO_RURAL", 0.10);   // 10 + 5*(E mod 4)      = 10 + 0 = 10 %
        CONFIG.put("RECARGO_NACIONAL", 0.40); // 40 + 5*(F mod 3)     = 40 + 0 = 40 %
        CONFIG.put("DESC_VIP", 0.10);        // 10 + 5*(A mod 3)      = 10 + 0 = 10 %
        CONFIG.put("RECARGO_FINDE", 2000.0); // 2000 + 500*(C mod 4)  = 2000 + 0 = $2.000
    }

    // Nota: PMAX no existe en CONFIG; el codigo heredado compara contra 50 en duro.
    // Con el codigo estudiantil, PMAX = 30 + 5*A = 45, por lo que el 50 es un
    // defecto del codigo heredado que se reporta en la Parte 5 y se corrige en la Parte 6.


    /**
     * def calc(p, z, c, d, cup=None, flag=False):
     *
     * @param p  peso en kg (puede tener decimales)
     * @param z  zona: urbana | rural | nacional
     * @param c  cliente: normal | frecuente | vip
     * @param d  dia: 1 a 7 (6 = sabado, 7 = domingo)
     * @param cup cupon opcional
     * @param flag si es true imprime el costo
     * @return costo redondeado
     */
    static int calc(double p, String z, String c, int d, String cup, boolean flag) {
        if (p <= 0 || p > 50) {
            throw new IllegalArgumentException("peso invalido");
        }
        double t;
        if (p <= CONFIG.get("L1")) {
            t = 6000;
        } else if (p <= CONFIG.get("L2")) {
            t = 9000;
        } else {
            t = 9000 + (p - CONFIG.get("L2")) * 800;
        }
        if (z.equals("urbana")) {
            t = t;
        } else if (z.equals("rural")) {
            t = t + t * CONFIG.get("RECARGO_RURAL");
        } else if (z.equals("nacional")) {
            t = t + t * CONFIG.get("RECARGO_NACIONAL");
        }
        // fin de semana
        if (d > 6) {
            t = t + CONFIG.get("RECARGO_FINDE");
        }
        if (c.equals("frecuente")) {
            t = t - t * 0.05;
        }
        if (c.equals("vip")) {
            t = t - t * CONFIG.get("DESC_VIP");
        }
        if (cup != null) {
            if (cup.equals("ENVIO10")) {
                t = t - t * 0.10;
            }
        }
        if (t < 5000) {
            t = 5000;
        }
        if (flag) {
            System.out.println("costo: " + t);
        }
        return (int) (t / 100) * 100;
    }

    /** Sobrecarga equivalente al argumento por defecto flag=False. */
    static int calc(double p, String z, String c, int d, String cup) {
        return calc(p, z, c, d, cup, false);
    }

    /** Sobrecarga equivalente a los argumentos por defecto cup=None, flag=False. */
    static int calc(double p, String z, String c, int d) {
        return calc(p, z, c, d, null, false);
    }

    /**
     * Ejemplo del enunciado con los parametros del codigo estudiantil 0182310049:
     * peso = 4 kg, zona = rural, cliente = vip, dia = 6 (sabado), cupon = ENVIO10.
     *
     * Resultado esperado segun la especificacion (Parte 1): $9.600.
     * El codigo heredado devuelve otro valor; esa diferencia es el defecto que se
     * documenta en la Parte 5 y se corrige en la Parte 6.
     */
    public static void main(String[] args) {
        int resultado = calc(4, "rural", "vip", 6, "ENVIO10");
        System.out.println("resultado heredado: " + resultado);
        System.out.println("esperado por especificacion: 9600");
    }
}
