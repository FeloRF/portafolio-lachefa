package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de Usuarios.
 * Incluye métodos personalizados para la autenticación y perfilamiento de clientes.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     * Vital para el proceso de Login y para verificar registros duplicados.
     * @param email Correo a consultar.
     * @return Un Optional que contiene al usuario si existe.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Permite buscar usuarios por su rol (ADMIN o CLIENTE).
     * Útil para segmentar envíos de promociones.
     * @param rol Rol del usuario.
     * @return Lista de usuarios que coinciden con el rol.
     */
    java.util.List<Usuario> findByRol(cl.felorf.lachefa.portafoliobootcam.models.Rol rol);
}