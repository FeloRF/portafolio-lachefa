package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
// IMPORTANTE: Agregar estas dos importaciones para el Dashboard
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.models.Venta;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

/**
 * Controlador de Capa de Presentación para la gestión integral de productos.
 * Orquesta las peticiones HTTP, el filtrado de inventario y la analítica visual.
 * * @author Felipe Rojas Flores
 * @version 1.3
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	@Autowired
    private ProductoService productoService;

    /** Inyección necesaria para calcular los KPIs financieros en el Dashboard */
    @Autowired
    private VentaRepository ventaRepository; 

    /**
     * Renderiza el catálogo principal con Dashboard Administrativo.
     * Calcula en tiempo real el valor de bodega, ventas totales y variedad.
     */
	@GetMapping("/catalogo")
	public String listar(@RequestParam(name = "termino", required = false) String termino, Model model) {
	    
	    // 1. Obtención de productos (con o sin filtro)
	    List<Producto> productos = productoService.buscarPorNombre(termino);
	    
	 // 2. Obtención de productos en stock critico
	    long criticos = productoService.contarProductosEnLimiteStock();
	    model.addAttribute("criticos", criticos);
	    
	    // 3. Cálculo de KPIs para el Dashboard (Lógica Administrativa)
	    int valorBodega = productoService.calcularValorInventario();
	    
	    // Sumatoria de ingresos históricos
	    int totalVentas = ventaRepository.findAll().stream()
	            .mapToInt(v -> v.getPrecioVenta() * v.getCantidad())
	            .sum();
	            
	    int variedad = productos.size();

	    // 3. Obtención de Rankings de popularidad
	    Map<String, Long> estadisticas = productoService.obtenerExtremosVentas();

	    // 4. Poblamiento del modelo para Thymeleaf
	    model.addAttribute("productos", productos);
	    model.addAttribute("valorBodega", valorBodega);
	    model.addAttribute("totalVentas", totalVentas);
	    model.addAttribute("variedad", variedad);
	    
	    // Protección contra nulos para los badges de ranking
	    if (estadisticas != null) {
	        model.addAttribute("masVendidoId", estadisticas.get("masVendido"));
	        model.addAttribute("menosVendidoId", estadisticas.get("menosVendido"));
	    }

	    model.addAttribute("titulo", "Consola de Administración - La Chefa");
	    model.addAttribute("termino", termino);
	    
	    return "inventario";
	}
	
	
	/**
	 * Muestra el formulario para registrar un nuevo producto.
	 */
	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {
	    model.addAttribute("producto", new Producto());
	    model.addAttribute("titulo", "Registrar Nueva Salsa");
	    return "formulario";
	}

	/**
	 * Procesa el guardado de datos con validación de integridad.
	 * * @param producto Entidad vinculada al formulario.
	 * @param result   Resultado de las validaciones de @Valid.
	 */
	@PostMapping("/guardar")
	public String guardarProducto(@Valid Producto producto, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("titulo", "Error en el registro");
	        return "formulario";
	    }
	    
	    productoService.guardar(producto);
	    return "redirect:/productos/catalogo";
	}
	
	/**
	 * Ejecuta el borrado lógico de un recurso específico.
	 */
	@GetMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable Long id) {
	    productoService.borradoLogico(id);
	    return "redirect:/productos/catalogo";
	}
	
	/**
	 * Prepara la vista de edición cargando los datos del producto existente.
	 */
	@GetMapping("/editar/{id}")
	public String editarProducto(@PathVariable Long id, Model model) {
	    Producto producto = productoService.buscarPorId(id).orElse(null);
	    
	    if (producto == null) {
	        return "redirect:/productos/catalogo";
	    }
	    
	    model.addAttribute("producto", producto);
	    model.addAttribute("titulo", "Editar Salsa: " + producto.getNombre());
	    return "formulario";
	}
	
	/**
	 * Prepara la vista de los productos descatalogados o de temporada.
	 */
	@GetMapping("/papelera")
	public String verPapelera(Model model) {
	    model.addAttribute("productos", productoService.listarInactivos());
	    model.addAttribute("titulo", "Papelera de Reciclaje - La Chefa");
	    return "papelera"; // Crearemos este HTML pequeño
	}

	@GetMapping("/restaurar/{id}")
	public String restaurarProducto(@PathVariable Long id) {
	    productoService.restaurar(id);
	    return "redirect:/productos/papelera";
	}
}