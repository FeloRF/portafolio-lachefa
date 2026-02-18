package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Gestión de Usuarios y Clientes.
 * Centraliza la lógica de registro, perfilamiento y seguridad de acceso.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Nota: Cuando activemos Spring Security al 100%, aquí inyectaremos BCrypt
    // para encriptar las contraseñas antes de guardarlas.

    /**
     * Registra un nuevo usuario en el sistema.
     * Incluye validación de duplicidad de correo electrónico.
     * @param usuario Datos del nuevo integrante.
     * @return El usuario persistido.
     * @throws RuntimeException si el email ya está en uso.
     */
    @Transactional
    public Usuario registrar(Usuario usuario) {
        // Regla de Negocio: No pueden existir dos usuarios con el mismo email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El correo " + usuario.getEmail() + " ya está registrado.");
        }

        // Por defecto, si no se especifica, se registra como CLIENTE
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Recupera un usuario por su identificador único.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su credencial de correo.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Lista todos los usuarios con perfil de CLIENTE.
     * Ideal para el módulo de fidelización y promociones.
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRol(Rol.CLIENTE);
    }

    /**
     * Actualiza el perfil de un usuario existente.
     */
    @Transactional
    public Usuario actualizarPerfil(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
