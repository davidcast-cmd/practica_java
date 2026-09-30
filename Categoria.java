// EXTRA ELEGIDO: Categorías avanzadas.
// cada categoria lleva asociado su tipo de IVA
//  El carrito, la factura y los filtros con Streams comparten una
// única fuente
public enum Categoria {
    ELECTRONICA("Electrónica", 0.21),
    SOFTWARE("Software", 0.21),
    HOGAR("Hogar", 0.21),
    LIBROS("Libros", 0.04);

    private final String nombre;
    private final double iva;

    Categoria(String nombre, double iva) {
        this.nombre = nombre;
        this.iva = iva;
    }

    public String getNombre() {
        return nombre;
    }

    public double getIva() {
        return iva;
    }

    // IVA como porcentaje entero (0.21 -> 21), para mostrarlo
    public int getIvaPorcentaje() {
        return (int) Math.round(iva * 100);
    }

    public double calcularIva(double base) {
        return base * iva;
    }

    public double aplicarIva(double base) {
        return base + calcularIva(base);
    }
}
