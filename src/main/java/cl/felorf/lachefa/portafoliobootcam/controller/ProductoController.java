package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * Controlador para la gestión integral de productos y Dashboard Administrativo.
 * Coordina los KPIs financieros y el ranking de popularidad de salsas.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
	
    private final ProductoService productoService;
    private final VentaRepository ventaRepository;

    public ProductoController(ProductoService productoService, VentaRepository ventaRepository) {
        this.productoService = productoService;
        this.ventaRepository = ventaRepository;
    }

	@GetMapping("/catalogo")
	public String listar(@RequestParam(name = "termino", required = false) String termino, Model model) {
	    
	    // 1. Obtención de productos activos (con o sin filtro)
	    List<Producto> productos = productoService.buscarPorNombre(termino);
	    
	    // 2. KPIs de Inventario
	    long criticos = productoService.contarProductosEnLimiteStock();
	    int valorBodega = productoService.calcularValorInventario();
	    int variedad = productos.size();

	    // 3. KPI de Ventas Totales (Asegurando consistencia con Integer para CLP)
	    int totalVentas = ventaRepository.findAll().stream()
	    	    .map(Venta::getTotal)              // Extrae los totales de las ventas
	    	    .filter(java.util.Objects::nonNull) // ELIMINA LOS NULOS: Esto evita el colapso
	    	    .mapToInt(Integer::intValue)
	    	    .sum();

	    // 4. Ranking de Popularidad (Lógica avanzada del VentaRepository)
	    Map<Producto, Long> rankingVentas = productoService.obtenerRankingProductos();

	    // 5. Poblamiento del modelo para la vista 'inventario.html'
	    model.addAttribute("productos", productos);
	    model.addAttribute("valorBodega", valorBodega);
	    model.addAttribute("totalVentas", totalVentas);
	    model.addAttribute("variedad", variedad);
	    model.addAttribute("criticos", criticos);
	    model.addAttribute("ranking", rankingVentas);
	    model.addAttribute("titulo", "Consola de Administración - La Chefa");
	    model.addAttribute("termino", termino);
	    
	    return "inventario";
	}
	
	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {
	    model.addAttribute("producto", new Producto());
	    model.addAttribute("titulo", "Registrar Nueva Salsa");
	    return "formulario";
	}

	@PostMapping("/guardar")
	public String guardarProducto(@Valid Producto producto, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("titulo", "Error en el registro");
	        return "formulario";
	    }
	    productoService.guardar(producto);
	    return "redirect:/productos/catalogo";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable Long id) {
	    productoService.borradoLogico(id);
	    return "redirect:/productos/catalogo";
	}
	
	@GetMapping("/editar/{id}")
	public String editarProducto(@PathVariable Long id, Model model) {
	    Producto producto = productoService.buscarPorId(id).orElse(null);
	    if (producto == null) return "redirect:/productos/catalogo";
	    
	    model.addAttribute("producto", producto);
	    model.addAttribute("titulo", "Editar Salsa: " + producto.getNombre());
	    return "formulario";
	}
	
	@GetMapping("/papelera")
	public String verPapelera(Model model) {
	    model.addAttribute("productos", productoService.listarInactivos());
	    model.addAttribute("titulo", "Papelera de Reciclaje - La Chefa");
	    return "papelera";
	}

	@GetMapping("/restaurar/{id}")
	public String restaurarProducto(@PathVariable Long id) {
	    productoService.restaurar(id);
	    return "redirect:/productos/papelera";
	}
}