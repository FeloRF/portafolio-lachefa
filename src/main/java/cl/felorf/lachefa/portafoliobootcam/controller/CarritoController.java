package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.services.CarritoService;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;

    public CarritoController(CarritoService carritoService, ProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session, RedirectAttributes flash) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            if (producto.getStock() > 0) {
                carritoService.agregarProducto(producto, 1);
                actualizarContadorSesion(session); // Método centralizado
                flash.addFlashAttribute("success", "¡" + producto.getNombre() + " agregada!");
            } else {
                flash.addFlashAttribute("error", "Sin stock de " + producto.getNombre());
            }
        }
        return "redirect:/catalogoLaChefa"; 
    }

    @GetMapping("/ver")
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.calcularTotal());
        model.addAttribute("titulo", "Tu Carrito de La Chefa");
        return "tienda/carrito"; 
    }
    
    @GetMapping("/incrementar/{id}")
    public String incrementar(@PathVariable Long id, HttpSession session) {
        carritoService.actualizarCantidad(id, 1);
        actualizarContadorSesion(session);
        return "redirect:/carrito/ver";
    }

    @GetMapping("/decrementar/{id}")
    public String decrementar(@PathVariable Long id, HttpSession session) {
        carritoService.actualizarCantidad(id, -1);
        actualizarContadorSesion(session);
        return "redirect:/carrito/ver";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        carritoService.eliminarItem(id);
        actualizarContadorSesion(session); // Sincronizamos el badge
        return "redirect:/carrito/ver";
    }

    /**
     * Limpia todo el carrito de una vez
     * Requerido por el botón 'Vaciar Carrito' en carrito.html
     */
    @GetMapping("/limpiar")
    public String limpiarCarrito(HttpSession session) {
        carritoService.limpiarCarrito();
        session.setAttribute("itemsEnCarrito", 0); // Reset a cero
        return "redirect:/catalogoLaChefa";
    }

    /**
     * Helper Method: Centraliza la lógica del contador para evitar errores de tipeo
     */
    private void actualizarContadorSesion(HttpSession session) {
        int totalItems = carritoService.getItems().stream()
                                     .mapToInt(item -> item.getCantidad())
                                     .sum();
        session.setAttribute("itemsEnCarrito", totalItems); // Nombre clave para fragments.html
    }
}