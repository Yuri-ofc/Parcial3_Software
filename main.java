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
     * CONFIG = {
     *   "L1": None,            # <- pon tus valores
     *   "L2": None,
     *   "RECARGO_RURAL": None, # como fraccion, ej. 0.15
     *   "RECARGO_NACIONAL": None,
     *   "DESC_VIP": None,
     *   "RECARGO_FINDE": None, # en pesos
     * }
     */
    static final Map<String, Double> CONFIG = new HashMap<>();

    static {
        CONFIG.put("L1", null);              // <- pon tus valores
        CONFIG.put("L2", null);
        CONFIG.put("RECARGO_RURAL", null);   // como fraccion, ej. 0.15
        CONFIG.put("RECARGO_NACIONAL", null);
        CONFIG.put("DESC_VIP", null);
        CONFIG.put("RECARGO_FINDE", null);   // en pesos
    }

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
     * Ejemplo del enunciado (codigo estudiantil 2023184706), solo para verificar
     * que la traduccion se comporta igual que el Python original.
     * Reemplaza estos valores por los de TU codigo.
     */
    public static void main(String[] args) {
        CONFIG.put("L1", 4.0);              // 2 + (B mod 3), B = 8
        CONFIG.put("L2", 14.0);             // 10 + C, C = 4
        CONFIG.put("RECARGO_RURAL", 0.10);  // 10 + 5*(E mod 4), E = 0
        CONFIG.put("RECARGO_NACIONAL", 0.40); // 40 + 5*(F mod 3), F = 6
        CONFIG.put("DESC_VIP", 0.15);       // 10 + 5*(A mod 3), A = 1
        CONFIG.put("RECARGO_FINDE", 2000.0); // 2000 + 500*(C mod 4), C = 4

        int resultado = calc(4, "rural", "vip", 6, "ENVIO10");
        System.out.println("resultado: " + resultado);
    }
}
