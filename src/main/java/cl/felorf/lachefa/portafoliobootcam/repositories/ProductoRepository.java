package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Producto.
 * Al extender de JpaRepository, obtenemos acceso a métodos como save(), 
 * findAll(), findById() y delete() sin necesidad de implementar SQL manual.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	/**
     * Busca productos por nombre (coincidencia parcial) que estén activos.
     * * @param nombre Texto a buscar.
     * @return Lista de productos que contienen el texto, sin importar mayúsculas.
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    List<Producto> findByNivelPicorBetween(Integer min, Integer max); // nivel de picor
}