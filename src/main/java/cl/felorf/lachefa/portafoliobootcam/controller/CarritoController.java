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

    // Inyección por Constructor: Estándar de inmutabilidad y testeo
    public CarritoController(CarritoService carritoService, ProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    /**
     * Agrega un producto al carrito desde el catálogo.
     */
    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, 
                                   HttpSession session, 
                                   RedirectAttributes flash) {
        
        Optional<Producto> productoOpt = productoService.buscarPorId(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            
            // Verificamos stock antes de agregar (Red Team Mindset)
            if (producto.getStock() > 0) {
                carritoService.agregarProducto(producto, 1);
                
                // Actualizamos el contador de la sesión para el badge del Navbar
                int totalItems = carritoService.getItems().stream()
                                               .mapToInt(item -> item.getCantidad())
                                               .sum();
                session.setAttribute("itemsEnCarrito", totalItems);
                
                flash.addFlashAttribute("success", "¡" + producto.getNombre() + " agregada al carrito!");
            } else {
                flash.addFlashAttribute("error", "Lo sentimos, nos quedamos sin stock de " + producto.getNombre());
            }
        } else {
            flash.addFlashAttribute("error", "El producto solicitado no existe.");
        }

        // Redirigimos de vuelta al catálogo para que el usuario siga comprando
        return "redirect:/catalogoLaChefa"; 
    }

    /**
     * Muestra la vista detallada del carrito.
     */
    @GetMapping("/ver")
    public String verCarrito(Model model) {
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", carritoService.calcularTotal());
        model.addAttribute("titulo", "Tu Carrito de La Chefa");
        return "tienda/carrito"; 
    }

    /**
     * Elimina un item del carrito.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        carritoService.eliminarItem(id);
        
        // Actualizamos el contador del Navbar tras eliminar
        int totalItems = carritoService.getItems().stream()
                                       .mapToInt(item -> item.getCantidad())
                                       .sum();
        session.setAttribute("itemsEnCarrito", totalItems);
        
        return "redirect:/carrito/ver";
    }
}