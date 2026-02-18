package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.services.CarritoService;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión del flujo del carrito de compras.
 * Orquesta la interacción entre el catálogo de productos y la sesión del cliente.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    /**
     * Agrega un producto al carrito de sesión.
     * Si el producto ya existe, incrementa la cantidad en 1.
     * * @param id ID del producto seleccionado desde la vista.
     * @return Redirección a la vista del carrito.
     */
    @GetMapping("/agregar/{id}")
    public String agregarItem(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id).orElse(null);
        
        if (producto != null && producto.isActivo() && producto.getStock() > 0) {
            // Agregamos 1 unidad por defecto al hacer clic en el catálogo
            carritoService.agregarProducto(producto, 1);
        }
        
        return "redirect:/carrito/ver";
    }

    /**
     * Renderiza la vista detallada del carrito con subtotales y total general.
     * * @param model Contenedor para pasar la lista de items y el total a Thymeleaf.
     * @return Template 'carrito.html'.
     */
    @GetMapping("/ver")
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.calcularTotal());
        model.addAttribute("titulo", "Tu Carrito - La Chefa");
        return "carrito";
    }

    /**
     * Elimina un producto específico del carrito.
     * * @param id ID del producto a remover.
     * @return Redirección a la vista del carrito actualizada.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return "redirect:/carrito/ver";
    }

    /**
     * Vacía completamente el carrito del cliente y lo devuelve a la tienda.
     * @return Redirección a la raíz de la tienda pública.
     */
    @GetMapping("/limpiar")
    public String limpiarCarrito() {
        carritoService.limpiarCarrito();
     
        return "redirect:/catalogoLaChefa"; 
    }
}