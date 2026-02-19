package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de Usuarios.
 * Incluye métodos personalizados para la autenticación y perfilamiento de clientes.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
     * Busca un usuario en la base de datos utilizando su correo electrónico.
     * Usamos Optional para manejar de forma segura los casos donde el usuario no existe.
     * * @param email El correo a buscar.
     * @return Un Optional que contiene al Usuario si se encuentra.
     */
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);

   
    boolean existsByEmail(String email);
}