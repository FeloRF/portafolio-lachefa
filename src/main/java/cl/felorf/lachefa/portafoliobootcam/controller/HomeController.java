package cl.felorf.lachefa.portafoliobootcam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador Principal: Punto de entrada único para todos los visitantes.
 * Gestiona la navegación hacia los perfiles de Admin, Cliente e Invitado.
 */

@Controller
public class HomeController {
    @GetMapping("/") // Esta es la UNICA ruta para la raíz
    public String index(Model model) {
        model.addAttribute("titulo", "La Chefa - Inicio");
        return "home"; // Busca templates/home.html
    }
}
