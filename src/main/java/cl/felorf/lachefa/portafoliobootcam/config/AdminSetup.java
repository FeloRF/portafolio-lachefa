package cl.felorf.lachefa.portafoliobootcam.config;

import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSetup {

    /**
     * Bean que se ejecuta al iniciar la aplicación.
     * Genera un administrador con hash de BCrypt garantizado.
     */
    @Bean
    CommandLineRunner setupAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            String email = "admin@lachefa.cl";
            if (repo.findByEmail(email).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Admin La Chefa");
                admin.setEmail(email);
                admin.setUsername("admin");
                // Generamos el hash usando el encoder oficial del proyecto
                admin.setPassword(encoder.encode("admin123")); 
                admin.setRol(Rol.ADMIN);
                admin.setDireccion("Oficina Central");
                
                repo.save(admin);
                System.out.println("✅ NUEVO ADMIN CREADO: admin@lachefa.cl / admin123");
            }
        };
    }
}