package cl.felorf.lachefa.portafoliobootcam.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Venta.
 * Implementa consultas de navegación compleja para el ranking de productos.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
	
	/**
	 * Recupera todas las ventas ordenadas de manera descendente por fecha.
	 */
	List<Venta> findAllByOrderByFechaDesc();
	
	/**
	 * Navegación por propiedades: Venta -> Detalles -> Producto -> Id.
	 */
	List<Venta> findByDetalles_Producto_Id(Long productoId);

	/**
	 * CORRECCIÓN CRÍTICA: 
	 * Como la Venta no tiene 'producto' directamente, debemos unirla con sus detalles (JOIN).
	 * Usamos el alias 'd' para referirnos a los elementos de la lista 'detalles'.
	 */
	@Query("SELECT d.producto.id, SUM(d.cantidad) " +
		       "FROM Venta v JOIN v.detalles d " +
		       "GROUP BY d.producto.id " +
		       "ORDER BY SUM(d.cantidad) DESC")
		List<Object[]> obtenerRankingVentas();
	
	/**
	 * Filtro temporal para el Dashboard administrativo.
	 */
	@Query("SELECT v FROM Venta v WHERE v.fecha >= :fecha")
    List<Venta> buscarVentasRecientes(@Param("fecha") LocalDateTime fecha);
}