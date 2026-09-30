import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Guarda los productos en un Map con el id como clave
// Garantiza que no haya ids duplicados porque no admite repes
// LinkedHashMap mantiene el orden de alta en los listados
public class ProductoService {
    private Map<Integer, Producto> productos = new LinkedHashMap<>();

    // Da de alta un producto; lanza excepción si los datos no son válidos o si su id ya existe
    public void alta(Producto producto) throws IdDuplicadoException, DatoInvalidoException {
        validar(producto);
        if (productos.containsKey(producto.getId())) {
            throw new IdDuplicadoException("Ya existe un producto con id " + producto.getId());
        }
        productos.put(producto.getId(), producto);
    }

    // Comprueba que los datos del producto tengan sentido
    private void validar(Producto p) throws DatoInvalidoException {
        if (p.getId() <= 0) {
            throw new DatoInvalidoException("El id debe ser mayor que 0");
        }
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre no puede estar vacío");
        }
        if (p.getCategoria() == null) {
            throw new DatoInvalidoException("El producto debe tener categoría");
        }
        if (p.getPrecio() < 0) {
            throw new DatoInvalidoException("El precio no puede ser negativo");
        }
        if (p.getStock() < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo");
        }
    }

    // Elimina un producto por id, lanza excepción si no existe
    public void baja(int id) throws ProductoNoEncontradoException {
        if (!productos.containsKey(id)) {
            throw new ProductoNoEncontradoException("No existe ningún producto con id " + id);
        }
        productos.remove(id);
    }

    // Devuelve el producto con ese id, get devuelve null si no existe
    public Producto buscarPorId(int id) throws ProductoNoEncontradoException {
        Producto producto = productos.get(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException("No existe ningún producto con id " + id);
        }
        return producto;
    }

    // Muestra todos los productos
    public void listar() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos");
            return;
        }
        for (Producto p : productos.values()) {
            System.out.println(p.getId() + " | " + p.getNombre() + " | "
                    + p.getCategoria().getNombre() + " (IVA " + p.getCategoria().getIvaPorcentaje() + "%) | "
                    + p.getPrecio() + " EUR | stock: " + p.getStockTexto());
        }
    }

    // copia de todos los productos (útil para estadísticas o exportar)
    public Collection<Producto> getTodos() {
        return new ArrayList<>(productos.values());
    }

    // Filtra por categoría con Streams
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        return productos.values().stream()
                .filter(p -> p.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    // Cuenta los productos de cada categoría con Streams
    public Map<Categoria, Long> contarPorCategoria() {
        return productos.values().stream()
                .collect(Collectors.groupingBy(Producto::getCategoria,
                        () -> new EnumMap<>(Categoria.class), Collectors.counting()));
    }

    // Filtra por rango de precio con Streams
    public List<Producto> buscarPorPrecio(double min, double max) {
        return productos.values().stream()
                .filter(p -> p.getPrecio() >= min && p.getPrecio() <= max)
                .collect(Collectors.toList());
    }
}
