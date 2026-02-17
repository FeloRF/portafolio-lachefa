package cl.felorf.lachefa.portafoliobootcam.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import java.util.List;


/**
 * Repositorio para la entidad Venta.
 * Permite la persistencia del historial de transacciones y consultas
 * de rendimiento de ventas.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
	
	/**
	 * Recupera todas las ventas ordenadas de manera descendente por fecha
	 ** @return Lista de Ventas cronologicamente.
	 */
	List<Venta> findAllByOrderByFechaDesc();
	
	/**
	 * Consulta para Buscar las ventas de algun producto especifico.
	 * Util para saber el rendimiento de los productos
	 * *@Param producto ID del producto a consultar.
	 * @return Lista de ventas asociadas al ID
	 */
	List<Venta> findByProductoId(Long productoId);

	@Query("SELECT v.producto.id, SUM(v.cantidad) FROM Venta v GROUP BY v.producto.id ORDER BY SUM(v.cantidad) DESC")
	List<Object[]> obtenerRankingVentas();
}
