package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controlador para la vista pública de la tienda.
 * Gestiona el catálogo que ven los clientes e invitados.
 * * @author Felipe Rojas Flores
 */
@Controller
public class TiendaController {
	
	@Autowired
	private ProductoService productoService;
	
	/**
	 * Renderiza el catálogo principal. 
	 * Soporta ambas rutas ("/" y "/catalogo") para mejorar el SEO y la navegación.
	 * * @param termino Palabra clave opcional para filtrar productos.
	 * @param model Contenedor de datos para la vista.
	 * @return La vista 'tienda/catalogo.html' con la lista de productos.
	 */
	@GetMapping("/catalogoLaChefa") 
	public String verCatalogo(@RequestParam(name = "termino", required = false) String termino, Model model) {
        List<Producto> productos = productoService.buscarPorNombre(termino);
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Catálogo de Salsas | La Chefa");
        return "tienda/catalogo"; // Busca templates/tienda/catalogo.html
    }
}