public class ProductoFisico extends Producto {
    private double peso;
    private double gastosEnvio;

    public ProductoFisico(int id, String nombre, Categoria categoria, double precio, int stock, double peso,
            double gastosEnvio) {
        super(id, nombre, categoria, precio, stock);

        this.peso = peso;
        this.gastosEnvio = gastosEnvio;
    }

    public double getPeso() {
        return peso;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio() + gastosEnvio;
    }
}