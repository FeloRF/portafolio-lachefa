package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.*;
import cl.felorf.lachefa.portafoliobootcam.services.*;
import jakarta.servlet.http.HttpSession; // IMPORTANTE: Debes agregar este import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Orquestador del proceso de pago y cierre de orden.
 * Asegura la integridad entre la sesión del carrito y la persistencia en DB.
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final VentaService ventaService;

    public CheckoutController(CarritoService carritoService, 
                              UsuarioService usuarioService, 
                              VentaService ventaService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
        this.ventaService = ventaService;
    }

    /**
     * Prepara la vista de confirmación con los datos del carrito.
     */
    @GetMapping("/confirmar")
    public String mostrarCheckout(Model model) {
        if (carritoService.getItems().isEmpty()) {
            // CORRECCIÓN: Redirigimos al catálogo PÚBLICO para evitar el login de admin
            return "redirect:/catalogoLaChefa"; 
        }

        Integer totalCalculado = carritoService.calcularTotal();
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", totalCalculado);
        model.addAttribute("titulo", "Finalizar Compra - La Chefa");

        return "tienda/confirmar"; 
    }

    /**
     * Procesa la transacción final y limpia el estado visual del Navbar.
     */
    @PostMapping("/pagar")
    public String procesarPago(@RequestParam String nombre, 
                               @RequestParam String email, 
                               @RequestParam String direccion,
                               HttpSession session, // Necesario para limpiar el badge
                               RedirectAttributes flash,
                               Model model) {
        
        if (nombre.isBlank() || email.isBlank() || direccion.isBlank()) {
            flash.addFlashAttribute("error", "Todos los campos de despacho son obligatorios.");
            return "redirect:/checkout/confirmar";
        }

        try {
            Usuario cliente = usuarioService.obtenerOCrearUsuarioInvitado(email, nombre, direccion);
            
            List<CarritoItem> itemsActuales = carritoService.getItems();
            Integer totalFinal = carritoService.calcularTotal();
            
            Venta ventaFinalizada = ventaService.generarVenta(itemsActuales, cliente, totalFinal);

            // 1. Limpieza lógica (Servicio)
            carritoService.limpiarCarrito();

            // 2. LIMPIEZA VISUAL (Sesión): El cambio clave para el icono
            // Al ponerlo en 0, la condición th:if del fragmento se oculta automáticamente.
            session.setAttribute("itemsEnCarrito", 0);

            model.addAttribute("venta", ventaFinalizada);
            model.addAttribute("titulo", "¡Gracias por tu compra en La Chefa!");
            
            return "tienda/exito";

        } catch (RuntimeException e) {
            flash.addFlashAttribute("error", "No pudimos procesar tu orden: " + e.getMessage());
            return "redirect:/checkout/confirmar";
        }
    }
}