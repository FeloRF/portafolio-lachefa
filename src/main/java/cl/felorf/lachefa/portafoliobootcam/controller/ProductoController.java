package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import java.util.List;
import java.util.Map;

/**
 * Controlador de Capa de Presentación para la gestión integral de productos.
 * Orquesta las peticiones HTTP, el filtrado de inventario y la analítica visual.
 * * @author Felipe Rojas Flores
 * @version 1.2
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	@Autowired
    private ProductoService productoService;

    /**
     * Renderiza el catálogo principal. Integra el motor de búsqueda y el 
     * cálculo de KPIs (Productos más y menos vendidos).
     * * @param termino Texto opcional para filtrar productos por nombre.
     * @param model   Contenedor para el paso de datos a Thymeleaf.
     * @return Template 'inventario.html'.
     */
    @GetMapping("/catalogo")
    public String listar(@RequestParam(name = "termino", required = false) String termino, Model model) {
        
        // 1. Obtención de la data filtrada
        List<Producto> productos = productoService.buscarPorNombre(termino);
        
        // 2. Cálculo de analítica de ventas (Best Sellers / Slow Movers)
        Map<String, Long> estadisticas = productoService.obtenerExtremosVentas();
        
        // 3. Inyección de atributos al modelo
        model.addAttribute("productos", productos);
        
        // Verificamos que las estadísticas no sean nulas antes de pasarlas
        if (estadisticas != null) {
            model.addAttribute("masVendidoId", estadisticas.get("masVendido"));
            model.addAttribute("menosVendidoId", estadisticas.get("menosVendido"));
        }
        
        model.addAttribute("titulo", "Panel de Gestión - La Chefa");
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
}