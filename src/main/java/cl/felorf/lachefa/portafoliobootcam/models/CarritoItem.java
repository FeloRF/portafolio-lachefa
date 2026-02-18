package cl.felorf.lachefa.portafoliobootcam.models;

/**
 * Representa un elemento individual dentro del carrito de compras.
 * Almacena el producto seleccionado y la cantidad deseada.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
public class CarritoItem {

    private Producto producto;
    private Integer cantidad;

    /**
     * Constructor para inicializar un item del carrito.
     * @param producto Objeto Producto completo.
     * @param cantidad Unidades seleccionadas.
     */
    public CarritoItem(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Calcula el subtotal de esta línea (Precio x Cantidad).
     * @return El monto total del item.
     */
    public Integer getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    // ============================================
    // GETTERS Y SETTERS
    // ============================================

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}