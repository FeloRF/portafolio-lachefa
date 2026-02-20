package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.Rol; // <--- IMPORTANTE: Importar el Enum
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario() {
        return "registro"; 
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String direccion,
                                   RedirectAttributes flash) {
        
        // El servicio ya hace esta validación en el método 'registrar', 
        // pero dejarla aquí es una buena doble capa de seguridad.
        if (usuarioService.existePorEmail(email)) {
            flash.addFlashAttribute("error", "Ese correo ya está registrado.");
            return "redirect:/registro";
        }

        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreCompleto(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password); 
            nuevoUsuario.setDireccion(direccion);
            
            // SOLUCIÓN LÍNEA 52: Usamos el Enum Rol.CLIENTE en lugar de un String
            nuevoUsuario.setRol(Rol.CLIENTE); 

            // SOLUCIÓN LÍNEA 55: Llamamos a 'registrar' para que ejecute la lógica del servicio
            usuarioService.registrar(nuevoUsuario);

            flash.addFlashAttribute("success", "¡Cuenta creada! Ahora puedes entrar.");
            return "redirect:/login";

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}