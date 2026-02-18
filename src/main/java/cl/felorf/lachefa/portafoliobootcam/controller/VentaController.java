package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
// IMPORT CORRETO:
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador de Ventas: Gestiona el flujo transaccional y el historial financiero.
 * Implementa filtros temporales para la toma de decisiones administrativas.
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {
	
	@Autowired
	private ProductoService productoService; 
    
	@Autowired
	private VentaRepository ventaRepository; 
	
	/**
     * Procesa una venta rápida.
     * @param id ID del producto.
     * @return Redirección al catálogo.
     */
	@GetMapping("/vender/{id}")
    public String procesarVenta(@PathVariable Long id) {
        try {
            productoService.descontarStock(id, 1);
        } catch (RuntimeException e) {
            System.err.println("Error en transacción: " + e.getMessage());
        }
        return "redirect:/productos/catalogo";
    }
	
	/**
     * Renderiza el historial con filtros de tiempo (Hoy, Semana, Mes).
     */
	@GetMapping("/historial")
	public String verHistorial(@RequestParam(name = "periodo", required = false) String periodo, Model model) {
	    LocalDateTime inicio = null;
	    
	    if ("hoy".equals(periodo)) {
            // Desde las 00:00 de hoy
	        inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
	    } else if ("semana".equals(periodo)) {
	        inicio = LocalDateTime.now().minusWeeks(1);
	    } else if ("mes".equals(periodo)) {
	        inicio = LocalDateTime.now().minusMonths(1);
	    }

	    List<Venta> ventas;
	    if (inicio != null) {
            // Usamos el query personalizado del repositorio
	        ventas = ventaRepository.buscarVentasRecientes(inicio);
	    } else {
            // Si no hay filtro, traemos todo ordenado por fecha descendente
	        ventas = ventaRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
	    }

        // Calculamos el gran total del periodo para el dashboard
        int granTotal = ventas.stream().mapToInt(Venta::getTotalVenta).sum();

	    model.addAttribute("ventas", ventas);
        model.addAttribute("granTotal", granTotal);
	    model.addAttribute("titulo", "Historial de Ventas - Filtro: " + (periodo != null ? periodo : "Todos"));
	    
	    return "historial";
	}
}