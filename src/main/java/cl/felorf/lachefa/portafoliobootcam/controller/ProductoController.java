package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    private final ProductoService productoService;
    private final VentaRepository ventaRepository;

    public ProductoController(ProductoService productoService, VentaRepository ventaRepository) {
        this.productoService = productoService;
        this.ventaRepository = ventaRepository;
    }

    /**
     * CONSOLA DE ADMINISTRACIÓN (PANEL ADMIN)
     * URL: http://localhost:8081/productos/catalogo
     */
    @GetMapping("/catalogo")
    public String listar(@RequestParam(name = "termino", required = false) String termino, Model model) {
        // Obtenemos la lista una sola vez para optimizar
    	List<Producto> productos = productoService.buscarPorNombre(termino);
        if (productos == null) {
            productos = new java.util.ArrayList<>();
        }
        
        // KPIs Financieros y de Inventario
        long criticos = productoService.contarProductosEnLimiteStock();
        int valorBodega = productoService.calcularValorInventario();
        int variedad = productos.size();

        // Cálculo de ventas totales
        int totalVentas = 0;
        try {
            List<Venta> todasLasVentas = ventaRepository.findAll();
            if (todasLasVentas != null) {
                totalVentas = todasLasVentas.stream()
                        .map(Venta::getTotal)
                        .filter(Objects::nonNull) 
                        .mapToInt(Integer::intValue)
                        .sum();
            }
        } catch (Exception e) {
            totalVentas = 0; // Evita que el Error 500 rompa la página
        }

        Map<Producto, Long> rankingVentas = productoService.obtenerRankingProductos();

        // Inyección de atributos al modelo
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
    
    /**
     * FORMULARIO NUEVA SALSA
     * El botón en inventario.html debe apuntar a: th:href="@{/productos/nuevo}"
     */
    @GetMapping("/nuevo") 
    public String mostrarFormulario(Model model) {
        System.out.println(">>> ENTRANDO AL MÉTODO NUEVO"); // Agrega este log
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Registrar Nueva Salsa");
        return "formulario"; 
    }

    /**
     * ACCIÓN DE GUARDAR SALSA (Incluye URL de imagen)
     */
    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto, 
                                  BindingResult result, 
                                  Model model, 
                                  RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Error en el registro");
            return "formulario";
        }
        productoService.guardar(producto);
        flash.addFlashAttribute("success", "¡Salsa '" + producto.getNombre() + "' guardada correctamente! 🔥");
        return "redirect:/productos/catalogo";
    }
    
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        // Uso de orElse(null) para validación posterior
        Producto producto = productoService.buscarPorId(id).orElse(null);
        if (producto == null) return "redirect:/productos/catalogo";
        
        model.addAttribute("producto", producto);
        model.addAttribute("titulo", "Editar Salsa: " + producto.getNombre());
        return "formulario";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes flash) {
        productoService.borradoLogico(id);
        flash.addFlashAttribute("success", "Producto movido a la papelera.");
        return "redirect:/productos/catalogo";
    }

    /**
     * FORMULARIO NUEVA RECETA (DENTRO DEL CONTEXTO /productos)
     * El botón en inventario.html DEBE ser: th:href="@{/productos/nueva-receta}"
     */
    @GetMapping("/nueva-receta") 
    public String nuevaReceta(Model model) {
        model.addAttribute("receta", new Receta());
        model.addAttribute("titulo", "Publicar Nueva Receta");
        return "nueva-receta"; 
    }
    
    @GetMapping("/papelera")
    public String verPapelera(Model model) {
        model.addAttribute("productos", productoService.listarInactivos());
        model.addAttribute("titulo", "Papelera de Reciclaje - La Chefa");
        return "papelera";
    }

    @GetMapping("/restaurar/{id}")
    public String restaurarProducto(@PathVariable Long id, RedirectAttributes flash) {
        productoService.restaurar(id);
        flash.addFlashAttribute("success", "Producto restaurado con éxito.");
        return "redirect:/productos/papelera";
    }
}