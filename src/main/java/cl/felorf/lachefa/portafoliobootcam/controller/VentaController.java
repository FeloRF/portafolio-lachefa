package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador de Ventas: Gestiona el flujo transaccional y el historial financiero.
 * Centraliza las operaciones que afectan el patrimonio de la Pyme.
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {
	
	@Autowired
	private ProductoService productoService; //Lógica de descuento de stock.
	@Autowired
	private VentaRepository ventaRepository; // Consulta historial.
	
	/**
     * Procesa una venta rápida desde el catálogo.
     * Mueve la lógica de venta aquí para mantener el orden de responsabilidades.
     * @param id ID del producto vendido.
     * @return Redirección al catálogo de productos.
     */
	
	@GetMapping("/vender/{id}")
    public String procesarVenta(@PathVariable Long id) {
        try {
            // Ejecutamos la lógica transaccional (Descuento + Registro de Venta)
            productoService.descontarStock(id, 1);
        } catch (RuntimeException e) {
            // Log de error en consola si algo falla (ej: stock insuficiente)
            System.err.println("Error en transacción: " + e.getMessage());
        }
        // Redirigimos de vuelta al inventario (está en otro controlador)
        return "redirect:/productos/catalogo";
    }
	
	/**
     * Renderiza el historial de transacciones con el cálculo de ingresos totales.
     * @param model Contenedor de datos para Thymeleaf.
     * @return Vista 'historial.html'.
     */
	@GetMapping("/historial")
    public String verHistorial(Model model) {
		
		//1.Obtenemos las ventas ordenadas por fecha
		List<Venta> ventas = ventaRepository.findAllByOrderByFechaDesc();
		
		// 2. Cálculo de Gran Total (Streams)
        // Sumamos el resultado de getTotalVenta() de cada registro.
        int granTotal = ventas.stream()
                .mapToInt(Venta::getTotalVenta)
                .sum();

        // 3. Empaquetado de datos para la vista
        model.addAttribute("ventas", ventas);
        model.addAttribute("granTotal", granTotal);
        model.addAttribute("titulo", "Libro de Ventas - La Chefa");
        
        return "historial";
    }
}
		
		
	






















