import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidosService {
    private List<Pedido> historial = new ArrayList<>();
    private int siguienteId = 1;

    // crea un pedido a partir del carrito y lo guarda en el historial
    public Pedido crearPedido(Usuario usuario, Carrito carrito)
            throws CarritoVacioException, UsuarioInactivoException {
        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException("El usuario " + usuario.getNombre() + " está inactivo y no puede comprar");
        }
        if (carrito.estaVacio()) {
            throw new CarritoVacioException("No se puede cerrar un pedido con el carrito vacío");
        }
        Pedido pedido = new Pedido(siguienteId++, usuario, carrito);
        for (Map.Entry<Producto, Integer> e : carrito.getItems().entrySet()) {
            Producto p = e.getKey();
            p.setStock(p.getStock() - e.getValue());
        }
        historial.add(pedido);
        carrito.vaciar();
        return pedido;
    }

    public List<Pedido> getHistorial() {
        return historial;
    }

    // muestra las facturas guardadas
    public void mostrarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos en el historial");
            return;
        }
        for (Pedido p : historial) {
            p.imprimirFactura();
        }
    }
}
