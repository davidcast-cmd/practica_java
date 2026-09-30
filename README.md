Práctica Java

David Castellano Porras

---

1. Estructura de datos para evitar IDs duplicados (productos y usuarios)

Elección: Map<Integer, Producto> y Map<Integer, Usuario>, implementados con LinkedHashMap, donde la clave es el id.

Por qué es la mejor opción:
- En un mapa las claves son únicas por definición, así que usar el id como clave hace imposible tener dos elementos con el mismo id.
- Comprobar si un id existe (containsKey) y buscar por id (get) son operaciones directas, sin recorrer la colección.
- Uso LinkedHashMap y no HashMap porque mantiene el orden de alta en los listados. No uso TreeMap porque no necesito ordenar por id y sus operaciones son más lentas.

---

2. Estructura de datos para el contenido del carrito

Elección: Map<Producto, Integer> (LinkedHashMap), que relaciona cada producto con su cantidad.


Por qué es la mejor opción:
- El carrito es una relación producto → cantidad, y el mapa la representa directamente.
- No hay líneas duplicadas: si añado un producto que ya estaba, getOrDefault recupera la cantidad actual y se suma, en lugar de crear otra entrada.
- Saber cuántas unidades hay de un producto y quitarlo son operaciones directas, y eso permite validar el stock contando lo que ya hay en el carrito.
- LinkedHashMap conserva el orden en que se añadieron los productos, que es el que se ve al mostrar el carrito y en la factura.

---

3. Extra elegido: categorías avanzadas

Por qué lo he elegido: porque convierte la categoría en un concepto con comportamiento y no en una simple etiqueta de texto. Además evita errores de escritura (`"hogar"` frente a `"Hogar"`) y deja un único sitio donde está definido el IVA de cada categoría.

Cómo está integrado con el resto del proyecto:
- El enum Categoria lleva asociado su tipo de IVA (21 % en Electrónica, Software y Hogar; 4 % en Libros) y métodos como calcularIva() y aplicarIva().
- Producto.calcularPrecioConIva() usa el IVA de su categoría, y el carrito muestra sus totales con IVA incluido.
- La factura muestra el IVA de cada línea, la base imponible y un desglose del IVA por tipo, calculado con Streams (groupingBy).
- Las líneas del pedido guardan el IVA que tenía la categoría en el momento de la compra, así las facturas antiguas no cambian si se modifica el tipo después.
- Filtrado con Streams desde el menú de productos: buscarPorCategoria (filter) y el resumen por categoría con contarPorCategoria (groupingBy + counting).

> El IVA se aplica sobre el precio más los gastos de envío (base imponible).
