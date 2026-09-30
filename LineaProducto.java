public class LineaProducto {
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario; // sin IVA (precio + gastos de envío si los hay)
    private double tipoIva; // IVA de la categoría en el momento de la compra (0.21, 0.04...)

    // constructor
    public LineaProducto(String nombreProducto, int cantidad, double precioUnitario, double tipoIva) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoIva = tipoIva;
    }

    // getters
    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getTipoIva() {
        return tipoIva;
    }

    public int getIvaPorcentaje() {
        return (int) Math.round(tipoIva * 100);
    }

    // Base imponible de la línea (sin IVA)
    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    public double getImporteIva() {
        return getSubtotal() * tipoIva;
    }

    public double getTotalConIva() {
        return getSubtotal() + getImporteIva();
    }
}
