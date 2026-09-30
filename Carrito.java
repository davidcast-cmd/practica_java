import java.util.LinkedHashMap;
import java.util.Map;

public class Carrito {
    private Map<Producto, Integer> items = new LinkedHashMap<>();

    public void anadirProducto(Producto producto, int cantidad)
            throws CantidadInvalidaException, StockInsuficienteException {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe ser mayor que 0");
        }
        int enCarrito = items.getOrDefault(producto, 0);
        if (enCarrito + cantidad > producto.getStock()) {
            throw new StockInsuficienteException("Stock insuficiente de " + producto.getNombre()
                    + ": quedan " + producto.getStock() + " y ya tienes " + enCarrito + " en el carrito");
        }
        items.put(producto, enCarrito + cantidad);
    }

    public void quitarProducto(Producto producto) throws ProductoNoEncontradoException {
        if (!items.containsKey(producto)) {
            throw new ProductoNoEncontradoException("El producto " + producto.getNombre() + " no está en el carrito");
        }
        items.remove(producto);
    }

    // Total con IVA incluido (coincide con el total de la factura)
    public double calcularTotal() {
        double total = 0;
        for (Map.Entry<Producto, Integer> entrada : items.entrySet()) {
            total += entrada.getKey().calcularPrecioConIva() * entrada.getValue();
        }
        return total;
    }

    public void mostrarCarrito() {
        if (items.isEmpty()) {
            System.out.println("El carrito está vacío");
            return;
        }
        for (Map.Entry<Producto, Integer> entrada : items.entrySet()) {
            Producto p = entrada.getKey();
            int cantidad = entrada.getValue();
            System.out.printf("%-20.20s x%-4d %9.2f EUR c/u %10.2f EUR%n",
                    p.getNombre(), cantidad, p.calcularPrecioConIva(), p.calcularPrecioConIva() * cantidad);
        }
        System.out.printf("Total (IVA incluido): %.2f EUR%n", calcularTotal());
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public Map<Producto, Integer> getItems() {
        return items;
    }

    public void vaciar() {
        items.clear();
    }
}
