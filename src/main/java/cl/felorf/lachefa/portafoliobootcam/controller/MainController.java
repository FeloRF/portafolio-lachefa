package cl.felorf.lachefa.portafoliobootcam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * Esta es la única ruta que necesitamos aquí para evitar conflictos.
     * Ahora el error 404 desaparecerá sin romper el inicio del sitio.
     */
    @GetMapping("/en-construccion")
    public String irAMantenimiento() {
        return "construccion"; 
    }
}