package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.CarritoItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar el carrito de compras en la sesión del usuario.
 * Al estar anotado con @SessionScope, Spring mantiene una instancia única por cada visitante.
 * * @author Felipe Rojas Flores
 */
@Service
@SessionScope
public class CarritoService {

    /** Lista temporal de productos seleccionados por el cliente */
    private List<CarritoItem> items = new ArrayList<>();

    /**
     * Agrega un producto al carrito o incrementa su cantidad si ya existe.
     * @param producto El producto seleccionado del catálogo.
     * @param cantidad Cantidad deseada por el usuario.
     */
    public void agregarProducto(Producto producto, Integer cantidad) {
        for (CarritoItem item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new CarritoItem(producto, cantidad));
    }

    /**
     * Remueve un item completo del carrito.
     * @param productoId ID del producto a eliminar.
     */
    public void eliminarItem(Long productoId) {
        items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

    /**
     * Calcula el monto total acumulado en el carrito.
     * @return Suma de los subtotales de todos los items.
     */
    public Integer calcularTotal() {
        return items.stream()
                .mapToInt(CarritoItem::getSubtotal)
                .sum();
    }

    /**
     * Limpia el carrito después de una compra exitosa o por cancelación.
     */
    public void limpiarCarrito() {
        items.clear();
    }

    public List<CarritoItem> getItems() {
        return items;
    }
}