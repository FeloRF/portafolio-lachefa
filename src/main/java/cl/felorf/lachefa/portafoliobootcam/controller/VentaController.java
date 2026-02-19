package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador de Ventas: Gestiona el flujo transaccional y el historial financiero.
 * Implementa filtros temporales para la toma de decisiones administrativas.
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {
    
    // Inmutabilidad: atributos final para asegurar que no cambien tras la construcción
    private final ProductoService productoService; 
    private final VentaRepository ventaRepository; 

    /**
     * INYECCIÓN POR CONSTRUCTOR: Eliminamos @Autowired para cumplir con el estándar.
     * Esto facilita las pruebas unitarias y garantiza que el controlador no sea nulo.
     */
    public VentaController(ProductoService productoService, VentaRepository ventaRepository) {
        this.productoService = productoService;
        this.ventaRepository = ventaRepository;
    }

    /**
     * Procesa una venta rápida desde el panel de inventario.
     */
    @GetMapping("/vender/{id}")
    public String procesarVenta(@PathVariable Long id, RedirectAttributes flash) {
        try {
            productoService.descontarStock(id, 1);
            flash.addFlashAttribute("success", "Venta rápida procesada. ¡Stock actualizado!");
        } catch (RuntimeException e) {
            // Manejo de errores profesional con feedback al usuario
            flash.addFlashAttribute("error", "Error en la venta: " + e.getMessage());
        }
        return "redirect:/productos/catalogo";
    }
    
    /**
     * Renderiza el historial con filtros de tiempo y cálculos financieros precisos.
     */
    @GetMapping("/historial")
    public String verHistorial(@RequestParam(name = "periodo", required = false) String periodo, Model model) {
        LocalDateTime inicio = null;
        
        // Lógica de filtrado temporal
        if ("hoy".equals(periodo)) {
            inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if ("semana".equals(periodo)) {
            inicio = LocalDateTime.now().minusWeeks(1);
        } else if ("mes".equals(periodo)) {
            inicio = LocalDateTime.now().minusMonths(1);
        }

        List<Venta> ventas;
        if (inicio != null) {
            ventas = ventaRepository.buscarVentasRecientes(inicio);
        } else {
            ventas = ventaRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
        }

        /**
         * CORRECCIÓN CRÍTICA DE TIPOS: 
         * 1. Tu modelo 'Venta' usa getTotal().
         * 2. El total es Double, por lo que usamos mapToDouble y sumamos a una variable double.
         */
        int granTotal = ventas.stream()
                                 .mapToInt(Venta::getTotal)
                                 .sum();

        model.addAttribute("ventas", ventas);
        model.addAttribute("granTotal", granTotal);
        model.addAttribute("titulo", "Historial de Ventas - Filtro: " + (periodo != null ? periodo : "Todos"));
        
        return "historial";
    }
}