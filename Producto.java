public abstract class Producto {
    private int id;
    private String nombre;
    private Categoria categoria;
    private double precio;
    private int stock;

    // constructor
    public Producto(int id, String nombre, Categoria categoria, double precio, int stock) {

        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    // setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public abstract double calcularPrecioFinal();

    // Precio de una unidad con el IVA de su categoría (comportamiento del enum)
    public double calcularPrecioConIva() {
        return categoria.aplicarIva(calcularPrecioFinal());
    }

    public String getStockTexto() {
        return String.valueOf(stock);
    }

    // Identidad del producto = su id
    @Override
    public boolean equals(Object o) {
        return o instanceof Producto p && p.id == this.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}