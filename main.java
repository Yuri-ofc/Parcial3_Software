/** Fachada pública del cálculo y ejemplo de uso. */
public final class main {
    private main() { }

    public static int calc(double peso, String zona, String cliente, int dia, String cupon) {
        return Envios.calcularCosto(peso, zona, cliente, dia, cupon);
    }

    public static int calc(double peso, String zona, String cliente, int dia) {
        return calc(peso, zona, cliente, dia, null);
    }

    public static void main(String[] args) {
        System.out.println("Costo del envío: " + calc(4, "rural", "vip", 6, "ENVIO10"));
    }
}
