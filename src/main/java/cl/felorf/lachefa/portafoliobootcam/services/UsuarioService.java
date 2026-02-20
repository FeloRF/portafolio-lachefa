package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // REQUERIDO PARA SEGURIDAD
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Inyectamos el encriptador

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * LÓGICA DE REGISTRO REAL (Con Encriptación)
     */
    @Transactional
    public Usuario registrar(Usuario usuario) {
        String emailNormalizado = usuario.getEmail().toLowerCase().trim();
        
        if (usuarioRepository.findByEmail(emailNormalizado).isPresent()) {
            throw new RuntimeException("El correo ya está en nuestra base de datos de catadores.");
        }

        // ENCRIPTACIÓN: Nunca guardes texto plano
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEmail(emailNormalizado);
        
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * UPSERT PARA INVITADOS: Mantiene el flujo rápido del Checkout
     */
    @Transactional
    public Usuario obtenerOCrearUsuarioInvitado(String email, String nombre, String direccion) {
        String emailNormalizado = (email != null) ? email.toLowerCase().trim() : "";

        return usuarioRepository.findByEmail(emailNormalizado)
                .map(usuarioExistente -> {
                    usuarioExistente.setNombreCompleto(nombre);
                    usuarioExistente.setDireccion(direccion);
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseGet(() -> {
                    Usuario nuevoInvitado = new Usuario();
                    nuevoInvitado.setNombreCompleto(nombre);
                    nuevoInvitado.setEmail(emailNormalizado);
                    nuevoInvitado.setDireccion(direccion);
                    nuevoInvitado.setRol(Rol.CLIENTE);
                    // Los invitados no tienen password, pero el sistema requiere uno por defecto
                    nuevoInvitado.setPassword(passwordEncoder.encode("INVITADO_TEMP")); 
                    return usuarioRepository.save(nuevoInvitado);
                });
    }

    // --- MÉTODOS DE BÚSQUEDA (Limpios y sin duplicados) ---

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return (email == null) ? Optional.empty() : usuarioRepository.findByEmail(email.toLowerCase().trim());
    }

    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return buscarPorEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRol(Rol.CLIENTE);
    }

    @Transactional
    public Usuario actualizarPerfil(Usuario usuario) {
        // Aquí podrías agregar lógica para re-encriptar si el password cambió
        return usuarioRepository.save(usuario);
    }
}