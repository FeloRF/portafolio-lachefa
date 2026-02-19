package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.*;
import cl.felorf.lachefa.portafoliobootcam.services.*;
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

    // Inyección por Constructor: Mantenemos el estándar de inmutabilidad
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
        // Validación de guardia: No permitimos checkout con carrito vacío
        if (carritoService.getItems().isEmpty()) {
            return "redirect:/productos/catalogo"; 
        }

        // Recuperamos el total en Integer (Estandarizado para CLP)
        Integer totalCalculado = carritoService.calcularTotal();
        
        model.addAttribute("items", carritoService.getItems());
        model.addAttribute("total", totalCalculado);
        model.addAttribute("titulo", "Finalizar Compra - La Chefa");

        // CORRECCIÓN DE RUTA: Apuntamos a la carpeta 'tienda' según tu estructura
        return "tienda/confirmar"; 
    }

    /**
     * Procesa la transacción final, crea el usuario invitado y descuenta stock.
     */
    @PostMapping("/pagar")
    public String procesarPago(@RequestParam String nombre, 
                               @RequestParam String email, 
                               @RequestParam String direccion,
                               RedirectAttributes flash,
                               Model model) {
        
        // Red Team: Validación básica de campos obligatorios para evitar datos nulos en BD
        if (nombre.isBlank() || email.isBlank() || direccion.isBlank()) {
            flash.addFlashAttribute("error", "Todos los campos de despacho son obligatorios.");
            return "redirect:/checkout/confirmar";
        }

        try {
            // 1. Resolución del cliente (Lógica de 'Upsert' para evitar duplicados)
            Usuario cliente = usuarioService.obtenerOCrearUsuarioInvitado(email, nombre, direccion);

            /**
             * 2. PROCESAMIENTO TRANSACCIONAL:
             * Delegamos al servicio. Si el stock de alguna salsa falla, 
             * la anotación @Transactional hará Rollback automáticamente.
             */
            List<CarritoItem> itemsActuales = carritoService.getItems();
            Integer totalFinal = carritoService.calcularTotal();
            
            Venta ventaFinalizada = ventaService.generarVenta(itemsActuales, cliente, totalFinal);

            // 3. Limpieza de estado post-venta exitosa
            carritoService.limpiarCarrito();

            // 4. Preparación de la vista de éxito
            model.addAttribute("venta", ventaFinalizada);
            model.addAttribute("titulo", "¡Gracias por tu compra en La Chefa!");
            
            // Apuntamos a 'tienda/exito' que ya existe en tu carpeta
            return "tienda/exito";

        } catch (RuntimeException e) {
            // Capturamos errores de stock o de base de datos y notificamos al usuario
            flash.addFlashAttribute("error", "No pudimos procesar tu orden: " + e.getMessage());
            return "redirect:/checkout/confirmar";
        }
    }
}