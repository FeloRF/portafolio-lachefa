package cl.felorf.lachefa.portafoliobootcam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador Principal: Punto de entrada para el Home.
 * Se encarga únicamente de la landing page.
 * * @author Felipe Rojas Flores
 * @version 1.2
 */
@Controller
public class HomeController {

    /**
     * Punto de entrada raíz para www.lachefa.cl
     */
    @GetMapping("/") 
    public String index(Model model) {
        model.addAttribute("titulo", "La Chefa - Inicio");
        return "home"; 
    }

   
}