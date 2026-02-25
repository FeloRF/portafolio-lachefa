package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.services.RecetaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Controller
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    // ==========================================================
    // ZONA PÚBLICA (Accesible para todos)
    // ==========================================================

    /** * URL: localhost:8081/recetario 
     * Archivo físico: templates/recetario.html
     */
    @GetMapping("/recetario")
    public String listarRecetasPublicas(Model model) {
        model.addAttribute("recetas", recetaService.listarTodas());
        model.addAttribute("titulo", "Libro de Sabores 🌶️");
        return "recetario"; 
    }

    /** * URL: localhost:8081/recetario/paso-a-paso/{id}
     * Archivo físico: templates/tienda/detalle_receta.html
     */
    @GetMapping("/recetario/paso-a-paso/{id}")
    public String verPasoAPaso(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Optional<Receta> receta = recetaService.buscarPorId(id);
        if (receta.isEmpty()) {
            flash.addFlashAttribute("error", "Receta no encontrada.");
            return "redirect:/recetario";
        }
        model.addAttribute("receta", receta.get());
        return "tienda/detalle_receta"; 
    }

    // ==========================================================
    // ZONA ADMINISTRATIVA (Protegida por SecurityConfig)
    // ==========================================================

    /** * RUTA SOLICITADA: localhost:8081/nueva-receta 
     * Archivo físico: templates/nueva-receta.html
     */
    @GetMapping("/nueva-receta")
    public String nuevaReceta(Model model) {
        model.addAttribute("receta", new Receta());
        model.addAttribute("titulo", "Publicar Nueva Receta");
        return "nueva-receta";
    }

    /** * URL: localhost:8081/productos/recetas/editar/{id}
     */
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

    /** * ACCIÓN: Guardar datos y procesar imagen
     * th:action="@{/productos/recetas/guardar}"
     */
    @PostMapping("/productos/recetas/guardar")
    public String guardarReceta(@ModelAttribute Receta receta,
                                @RequestParam("archivoImagen") MultipartFile imagen,
                                RedirectAttributes flash) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                String rutaRelativa = "src/main/resources/static/img/recetas/";
                String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path rutaAbsoluta = Paths.get(rutaRelativa + nombreArchivo);

                Files.createDirectories(rutaAbsoluta.getParent());
                Files.copy(imagen.getInputStream(), rutaAbsoluta, StandardCopyOption.REPLACE_EXISTING);

                receta.setImagenUrl("/img/recetas/" + nombreArchivo);
            } else if (receta.getId() != null) {
                // Mantenemos la imagen existente en caso de edición
                recetaService.buscarPorId(receta.getId()).ifPresent(r -> receta.setImagenUrl(r.getImagenUrl()));
            }

            recetaService.guardar(receta);
            flash.addFlashAttribute("success", "¡Receta '" + receta.getNombre() + "' guardada! 🔥");

        } catch (IOException e) {
            flash.addFlashAttribute("error", "Error al procesar la imagen.");
            return "redirect:/nueva-receta";
        }
        return "redirect:/recetario";
    }

    /** * URL: localhost:8081/productos/recetas/eliminar/{id}
     */
    @GetMapping("/productos/recetas/eliminar/{id}")
    public String eliminarReceta(@PathVariable Long id, RedirectAttributes flash) {
        if (recetaService.existePorId(id)) {
            recetaService.eliminar(id);
            flash.addFlashAttribute("success", "Receta eliminada correctamente.");
        }
        return "redirect:/recetario";
    }
}