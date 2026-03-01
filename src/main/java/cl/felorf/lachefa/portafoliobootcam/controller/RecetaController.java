package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.services.RecetaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador para la gestión de recetas de La Chefa.
 * Implementa la redirección temporal a la página de construcción tras guardar.
 * * @author Felipe Rojas Flores (Felo)
 */
@Controller
public class RecetaController {

    private final RecetaService recetaService;

    /**
     * Inyección por constructor para favorecer la inmutabilidad.
     */
    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    // ==========================================================
    // ZONA PÚBLICA
    // ==========================================================

    @GetMapping("/recetario")
    public String listarRecetasPublicas(Model model) {
        model.addAttribute("recetas", recetaService.listarTodas());
        model.addAttribute("titulo", "Libro de Sabores 🌶️");
        return "recetario"; 
    }

    @GetMapping("/recetario/paso-a-paso/{id}")
    public String verPasoAPaso(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Optional<Receta> receta = recetaService.buscarPorId(id);
        if (receta.isEmpty()) {
            flash.addFlashAttribute("error", "Receta no encontrada.");
            return "redirect:/recetario";
        }
        model.addAttribute("receta", receta.get());
        return "redirect:/construccion"; 
    }
    

    // ==========================================================
    // ZONA ADMINISTRATIVA
    // ==========================================================

    @GetMapping("/nueva-receta")
    public String nuevaReceta(Model model) {
        model.addAttribute("receta", new Receta());
        model.addAttribute("titulo", "Publicar Nueva Receta");
        return "nueva-receta";
    }

    @GetMapping("/productos/recetas/editar/{id}")
    public String editarReceta(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Optional<Receta> receta = recetaService.buscarPorId(id);
        if (receta.isEmpty()) {
            flash.addFlashAttribute("error", "No se encontró la receta.");
            return "redirect:/recetario";
        }
        model.addAttribute("receta", receta.get());
        model.addAttribute("titulo", "Editar: " + receta.get().getNombre());
        return "nueva-receta";
    }

    /**
     * Procesa el guardado de la receta.

     */
    @PostMapping("/productos/recetas/guardar")
    public String guardarReceta(@ModelAttribute Receta receta, RedirectAttributes flash) {
        recetaService.guardar(receta);
        flash.addFlashAttribute("success", "¡Receta guardada!");
        // Redirección explícita a la URL mapeada
        return "redirect:/recetario"; 
    }

    @GetMapping("/productos/recetas/eliminar/{id}")
    public String eliminarReceta(@PathVariable Long id, RedirectAttributes flash) {
        if (recetaService.existePorId(id)) {
            recetaService.eliminar(id);
            flash.addFlashAttribute("success", "Receta eliminada correctamente.");
        }
        // CORRECCIÓN: Debe ser redirect para que pase por el filtro de seguridad correctamente
        return "redirect:/construccion"; 
    }
    
    /**
     * TICKET 001: Mapeo de la página en construcción (Fondo Blanco).
     */
    @GetMapping("/construccion")
    public String mostrarConstruccion() {
        return "construccion"; 
    }
}