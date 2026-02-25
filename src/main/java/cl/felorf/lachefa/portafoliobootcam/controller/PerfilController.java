package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Controlador para gestionar la vista del perfil del usuario logueado.
 * * @author Felipe Rojas Flores
 */
@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    // Inyección de dependencias por constructor
    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra la información del usuario actualmente autenticado.
     * * @param model Objeto para pasar datos a la vista Thymeleaf.
     * @param principal Objeto de Spring Security que contiene la identidad del usuario logueado.
     * @return El nombre del template 'perfil.html'.
     */
    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        // 1. Verificamos si hay una sesión activa (por seguridad adicional)
        if (principal == null) {
            return "redirect:/login";
        }

        /* * 2. Obtenemos el identificador del usuario. 
         * En nuestra configuración de JpaUserDetailsService, usamos el email como 'username' 
         * para Spring Security.
         */
        String emailLogueado = principal.getName();

        // 3. Buscamos los datos completos en la base de datos
        Usuario usuario = usuarioService.buscarPorEmail(emailLogueado)
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado en la base de datos."));

        // 4. Pasamos el objeto usuario a la vista
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Mi Perfil - La Chefa");

        return "perfil"; // Esto buscará src/main/resources/templates/perfil.html
    }
}