import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Pedido {
    private int id;
    private LocalDateTime fecha;
    private Usuario usuario;
    private List<LineaProducto> lineas = new ArrayList<>();
    private double baseImponible;
    private double totalIva;
    private double total; // base + IVA

    // constructor
    public Pedido(int id, Usuario usuario, Carrito carrito) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();

        // copia cada entrada del carrito a una línea del pedido
        for (Map.Entry<Producto, Integer> entrada : carrito.getItems().entrySet()) {
            // clave
            Producto p = entrada.getKey();
            // valor
            int cantidad = entrada.getValue();
            // Línea con nombre, cantidad, precio de una unidad (sin IVA) y el IVA de su categoría
            lineas.add(new LineaProducto(p.getNombre(), cantidad, p.calcularPrecioFinal(),
                    p.getCategoria().getIva()));
        }

        // base imponible e IVA sumando las líneas
        for (LineaProducto linea : lineas) {
            baseImponible += linea.getSubtotal();
            totalIva += linea.getImporteIva();
        }
        total = baseImponible + totalIva;
    }

    // getters
    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<LineaProducto> getLineas() {
        return lineas;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    public double getTotalIva() {
        return totalIva;
    }

    public double getTotal() {
        return total;
    }

    // Factura en formato tabla con desglose de IVA por tipo
    public void imprimirFactura() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String doble = "=".repeat(53);
        String simple = "-".repeat(53);

        System.out.println(doble);
        System.out.println(" ".repeat(23) + "FACTURA");
        System.out.println(doble);
        System.out.println("Pedido nº: " + id);
        System.out.println("Fecha:     " + fecha.format(formato));
        System.out.println("Cliente:   " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
        System.out.println(simple);
        System.out.printf("%-18s %5s %10s %5s %11s%n", "Producto", "Cant.", "Precio", "IVA", "Base");
        System.out.println(simple);

        for (LineaProducto linea : lineas) {
            // %-18.18s recorta los nombres largos para no descuadrar la tabla
            System.out.printf("%-18.18s %5d %10.2f %4d%% %11.2f%n",
                    linea.getNombreProducto(),
                    linea.getCantidad(),
                    linea.getPrecioUnitario(),
                    linea.getIvaPorcentaje(),
                    linea.getSubtotal());
        }

        System.out.println(simple);
        imprimirResumen("Base imponible", baseImponible);

        // IVA agrupado por tipo con Streams (TreeMap ordena por porcentaje)
        Map<Integer, Double> ivaPorTipo = lineas.stream()
                .collect(Collectors.groupingBy(LineaProducto::getIvaPorcentaje, TreeMap::new,
                        Collectors.summingDouble(LineaProducto::getImporteIva)));
        for (Map.Entry<Integer, Double> e : ivaPorTipo.entrySet()) {
            imprimirResumen("IVA " + e.getKey() + "%", e.getValue());
        }

        System.out.println(simple);
        imprimirResumen("TOTAL", total);
        System.out.println(doble);
    }

    private void imprimirResumen(String etiqueta, double valor) {
        System.out.printf("%-28s %24s%n", etiqueta, String.format("%.2f EUR", valor));
    }
}
