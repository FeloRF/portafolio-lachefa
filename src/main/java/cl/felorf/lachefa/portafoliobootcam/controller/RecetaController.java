package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.services.RecetaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/recetario")
public class RecetaController {

    private final RecetaService recetaService;

    // Inyectamos el servicio de recetas
    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /**
     * Muestra la lista general de recetas.
     * Esta ruta es PÚBLICA según nuestro SecurityConfig.
     */
    @GetMapping
    public String listarRecetas(Model model) {
        model.addAttribute("recetas", recetaService.listarTodas());
        model.addAttribute("titulo", "Recetario de La Chefa");
        return "recetario"; // Busca el archivo recetario.html en templates/
    }

    /**
     * Muestra el detalle de una receta específica.
     * Esta ruta está PROTEGIDA: Solo entran usuarios registrados.
     */
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Optional<Receta> recetaOpt = recetaService.buscarPorId(id);

        if (recetaOpt.isEmpty()) {
            flash.addFlashAttribute("error", "La receta solicitada no existe.");
            return "redirect:/recetario";
        }

        model.addAttribute("receta", recetaOpt.get());
        model.addAttribute("titulo", "Preparando: " + recetaOpt.get().getNombre());
        
        // Retornamos la nueva vista de detalle
        return "tienda/detalle_receta"; 
    }
}