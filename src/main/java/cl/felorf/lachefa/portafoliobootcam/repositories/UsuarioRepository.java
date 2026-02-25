package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Rol; 
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Usuarios: Maneja el acceso a datos para usuarios, invitados y administradores.
 * * @author Felipe Rojas Flores
 * @version 1.5
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	List<Usuario> findByRol(Rol rol);

    /**
     * Búsqueda para Login Híbrido.
     * @param username El alias del usuario (ej: 'admin')
     * @param email El correo electrónico (ej: 'felo@lachefa.cl')
     * @return Un Optional con el usuario si coincide con cualquiera de los dos campos.
     */
    Optional<Usuario> findByUsernameOrEmail(String username, String email);

    /** Búsqueda simple por email (Útil para validaciones de registro) */
    Optional<Usuario> findByEmail(String email);
    
    /** Búsqueda simple por username */
    Optional<Usuario> findByUsername(String username);
}