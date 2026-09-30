import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final ProductoService productos = new ProductoService();
    private final UsuarioService usuarios = new UsuarioService();
    private final PedidosService pedidos = new PedidosService();
    private final Carrito carrito = new Carrito();

    // MENÚ PRINCIPAL
    public void iniciar() {
        int opcion;
        do {
            System.out.println();
            System.out.println("===== GESTIÓN DE INVENTARIO =====");
            System.out.println("1. Gestionar productos");
            System.out.println("2. Gestionar usuarios");
            System.out.println("3. Gestionar carrito");
            System.out.println("4. Cerrar pedido / generar factura");
            System.out.println("5. Consultar historial de pedidos");
            System.out.println("0. Salir");
            opcion = leerInt("Opción: ");

            switch (opcion) {
                case 1 -> menuProductos();
                case 2 -> menuUsuarios();
                case 3 -> menuCarrito();
                case 4 -> cerrarPedido();
                case 5 -> pedidos.mostrarHistorial();
                case 0 -> System.out.println("Exit");
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    // Datos de ejemplo para probar
    public void cargarDatosDemo() {
        try {
            productos.alta(new ProductoFisico(1, "Teclado", Categoria.ELECTRONICA, 30.0, 5, 1.0, 10.0));
            productos.alta(new ProductoDigital(2, "Antivirus", Categoria.SOFTWARE, 40.0, 250));
            productos.alta(new ProductoFisico(3, "Lampara", Categoria.HOGAR, 25.5, 10, 2.0, 5.0));
            usuarios.alta(new Usuario(1, "Ana", "ana@mail.com", true));
            usuarios.alta(new Usuario(2, "Luis", "luis@mail.com", true));
        } catch (IdDuplicadoException | DatoInvalidoException e) {
            System.out.println("Error cargando datos de ejemplo: " + e.getMessage());
        }
    }

    // PRODUCTOS
    private void menuProductos() {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- PRODUCTOS ---");
            System.out.println("1. Alta");
            System.out.println("2. Baja");
            System.out.println("3. Listado");
            System.out.println("4. Buscar por categoría");
            System.out.println("5. Buscar por rango de precio");
            System.out.println("6. Resumen por categoría");
            System.out.println("0. Volver");
            opcion = leerInt("Opción: ");

            switch (opcion) {
                case 1 -> altaProducto();
                case 2 -> bajaProducto();
                case 3 -> productos.listar();
                case 4 -> buscarPorCategoria();
                case 5 -> buscarPorPrecio();
                case 6 -> resumenPorCategoria();
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private void altaProducto() {
        System.out.println("Tipo: 1) Físico  2) Digital");
        int tipo = leerInt("Tipo: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo no válido");
            return;
        }
        int id = leerInt("Id: ");
        String nombre = leerTexto("Nombre: ");
        Categoria categoria = leerCategoria();
        double precio = leerDouble("Precio: ");

        Producto p;
        if (tipo == 1) {
            int stock = leerInt("Stock: ");
            double peso = leerDouble("Peso (kg): ");
            double envio = leerDouble("Gastos de envío (EUR): ");
            if (peso < 0 || envio < 0) {
                System.out.println("Error: el peso y los gastos de envío no pueden ser negativos");
                return;
            }
            p = new ProductoFisico(id, nombre, categoria, precio, stock, peso, envio);
        } else {
            double tamano = leerDouble("Tamaño de descarga (MB): ");
            if (tamano < 0) {
                System.out.println("Error: el tamaño no puede ser negativo");
                return;
            }
            p = new ProductoDigital(id, nombre, categoria, precio, tamano);
        }

        try {
            productos.alta(p);
            System.out.println("Producto dado de alta");
        } catch (IdDuplicadoException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bajaProducto() {
        int id = leerInt("Id del producto a eliminar: ");
        try {
            productos.baja(id);
            System.out.println("Producto eliminado");
        } catch (ProductoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarPorCategoria() {
        Categoria categoria = leerCategoria();
        mostrarProductos(productos.buscarPorCategoria(categoria));
    }

    private void buscarPorPrecio() {
        double min = leerDouble("Precio mínimo: ");
        double max = leerDouble("Precio máximo: ");
        if (min > max) {
            System.out.println("Error: el mínimo no puede ser mayor que el máximo");
            return;
        }
        mostrarProductos(productos.buscarPorPrecio(min, max));
    }

    // Cuantos productos hay en cada categoría y que IVA lleva
    private void resumenPorCategoria() {
        Map<Categoria, Long> conteo = productos.contarPorCategoria();
        for (Categoria c : Categoria.values()) {
            System.out.printf("%-12s IVA %2d%% | %d producto(s)%n",
                    c.getNombre(), c.getIvaPorcentaje(), conteo.getOrDefault(c, 0L));
        }
    }

    private void mostrarProductos(List<Producto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay productos que coincidan");
            return;
        }
        for (Producto p : lista) {
            System.out.printf("%d | %s | %s (IVA %d%%) | %.2f EUR | stock: %s%n",
                    p.getId(), p.getNombre(), p.getCategoria().getNombre(),
                    p.getCategoria().getIvaPorcentaje(), p.getPrecio(), p.getStockTexto());
        }
    }

    // Muestra las categorías numeradas y devuelve la elegida
    private Categoria leerCategoria() {
        Categoria[] todas = Categoria.values();
        while (true) {
            System.out.println("Categorías:");
            for (int i = 0; i < todas.length; i++) {
                System.out.println((i + 1) + ". " + todas[i].getNombre());
            }
            int opcion = leerInt("Categoría: ");
            if (opcion >= 1 && opcion <= todas.length) {
                return todas[opcion - 1];
            }
            System.out.println("Categoría no válida");
        }
    }

    // USUARIOS
    private void menuUsuarios() {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- USUARIOS ---");
            System.out.println("1. Alta");
            System.out.println("2. Baja (desactivar)");
            System.out.println("3. Listado de activos");
            System.out.println("0. Volver");
            opcion = leerInt("Opción: ");

            switch (opcion) {
                case 1 -> altaUsuario();
                case 2 -> bajaUsuario();
                case 3 -> usuarios.listarActivos();
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private void altaUsuario() {
        int id = leerInt("Id: ");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        try {
            usuarios.alta(new Usuario(id, nombre, email, true));
            System.out.println("Usuario dado de alta");
        } catch (IdDuplicadoException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bajaUsuario() {
        int id = leerInt("Id del usuario a dar de baja: ");
        try {
            usuarios.baja(id);
            System.out.println("Usuario desactivado");
        } catch (UsuarioNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CARRITO
    private void menuCarrito() {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- CARRITO ---");
            System.out.println("1. Añadir producto");
            System.out.println("2. Quitar producto");
            System.out.println("3. Ver carrito y total");
            System.out.println("0. Volver");
            opcion = leerInt("Opción: ");

            switch (opcion) {
                case 1 -> anadirAlCarrito();
                case 2 -> quitarDelCarrito();
                case 3 -> carrito.mostrarCarrito();
                case 0 -> {
                }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private void anadirAlCarrito() {
        int id = leerInt("Id del producto: ");
        int cantidad = leerInt("Cantidad: ");
        try {
            Producto p = productos.buscarPorId(id);
            carrito.anadirProducto(p, cantidad);
            System.out.println("Añadido al carrito");
        } catch (ProductoNoEncontradoException | CantidadInvalidaException | StockInsuficienteException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void quitarDelCarrito() {
        int id = leerInt("Id del producto a quitar: ");
        try {
            Producto p = productos.buscarPorId(id);
            carrito.quitarProducto(p);
            System.out.println("Quitado del carrito");
        } catch (ProductoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CERRAR PEDIDO
    // El servicio valida carrito vacío y usuario inactivo, el menú solo muestra el
    // resultado
    private void cerrarPedido() {
        int idUsuario = leerInt("Id del usuario que compra: ");
        try {
            Usuario u = usuarios.buscarPorId(idUsuario);
            Pedido pedido = pedidos.crearPedido(u, carrito);
            pedido.imprimirFactura();
        } catch (UsuarioNoEncontradoException | CarritoVacioException | UsuarioInactivoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // LECTURA SEGURA
    // Todo se lee con nextLine() y se convierte, para no dejar saltos de línea
    // colgados
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private int leerInt(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido");
            }
        }
    }
}
