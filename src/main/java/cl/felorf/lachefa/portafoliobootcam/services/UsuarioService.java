package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Gestión de Usuarios y Clientes.
 * Implementa lógica de 'Upsert' para invitados y normalización de seguridad.
 */
@Service
public class UsuarioService {

    // Inmutabilidad: Definimos la dependencia como final
    private final UsuarioRepository usuarioRepository;

    /**
     * INYECCIÓN POR CONSTRUCTOR: 
     * Para cumplir la filosofía de POO y testeo.
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * LÓGICA DE CONVERSIÓN (Upsert): 
     * Busca un usuario por email o lo crea. Si ya existe, actualiza los datos de despacho.
     */
    @Transactional
    public Usuario obtenerOCrearUsuarioInvitado(String email, String nombre, String direccion) {
        // Red Team: Normalización estricta para evitar duplicados por mayúsculas/espacios
        String emailNormalizado = (email != null) ? email.toLowerCase().trim() : "";

        return usuarioRepository.findByEmail(emailNormalizado)
                .map(usuarioExistente -> {
                    // Actualización de datos de contacto/despacho en cada compra
                    usuarioExistente.setNombreCompleto(nombre);
                    usuarioExistente.setDireccion(direccion);
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseGet(() -> {
                    // Creación de perfil para nuevo cliente
                    Usuario nuevoInvitado = new Usuario();
                    nuevoInvitado.setNombreCompleto(nombre);
                    nuevoInvitado.setEmail(emailNormalizado);
                    nuevoInvitado.setDireccion(direccion);
                    nuevoInvitado.setRol(Rol.CLIENTE);
                    return usuarioRepository.save(nuevoInvitado);
                });
    }

    @Transactional
    public Usuario registrar(Usuario usuario) {
        String emailNormalizado = usuario.getEmail().toLowerCase().trim();
        
        if (usuarioRepository.findByEmail(emailNormalizado).isPresent()) {
            throw new RuntimeException("El correo ya está en nuestra base de datos de catadores.");
        }

        usuario.setEmail(emailNormalizado);
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null) return Optional.empty();
        return usuarioRepository.findByEmail(email.toLowerCase().trim());
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRol(Rol.CLIENTE);
    }

    @Transactional
    public Usuario actualizarPerfil(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}