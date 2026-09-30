public class ProductoDigital extends Producto {
    private double tamanoDescarga;

    public ProductoDigital(int id, String nombre, Categoria categoria, double precio, double tamanoDescarga) {
        super(id, nombre, categoria, precio, Integer.MAX_VALUE);

        this.tamanoDescarga = tamanoDescarga;
    }

    public double getTamanoDescarga() {
        return tamanoDescarga;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio();
    }

    @Override
    public String getStockTexto() {
        return "Ilimitado";
    }
}
