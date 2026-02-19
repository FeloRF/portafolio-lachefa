package cl.felorf.lachefa.portafoliobootcam.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador Principal: Punto de entrada único para todos los visitantes.
 * Gestiona la navegación hacia los perfiles de Admin, Cliente e Invitado.
 * * @author Felipe Rojas Flores
 * @version 1.1
 */
@Controller
public class HomeController {

    /**
     * Punto de entrada raíz para www.lachefa.cl
     */
    @GetMapping("/") 
    public String index(Model model) {
        model.addAttribute("titulo", "La Chefa - Inicio"); //
        return "home"; //
    }
    
    /**
     * Sección de recetas que integra los productos del catálogo.
     */
    @GetMapping("/recetario")
    public String recetario(Model model) {
        model.addAttribute("titulo", "Recetario La Chefa"); //
        
        // Datos de prueba: Estructura de Map para simular registros de BD
        List<Map<String, String>> recetas = List.of(
            Map.of(
                "nombre", "Tacos al Pastor Caseros", 
                "dificultad", "Media", 
                "tiempo", "45 min", 
                "salsa", "Habanero Infierno"
            ),
            Map.of(
                "nombre", "Alitas Spicy BBQ", 
                "dificultad", "Fácil", 
                "tiempo", "30 min", 
                "salsa", "Chipotle Ahumado"
            ),
            Map.of(
                "nombre", "Ceviche de Mango y Chile", 
                "dificultad", "Baja", 
                "tiempo", "15 min", 
                "salsa", "Jalapeño Verde"
            )
        ); //
        
        model.addAttribute("recetas", recetas); //
        return "recetario"; //
    }
}