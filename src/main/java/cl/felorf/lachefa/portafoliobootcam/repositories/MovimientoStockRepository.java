package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.MovimientoStock;
import cl.felorf.lachefa.portafoliobootcam.models.PuntoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    
    List<MovimientoStock> findByPuntoVenta(PuntoVenta puntoVenta);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM MovimientoStock m WHERE m.tipo IN :tipos AND m.fecha >= :fecha")
    Integer sumarCantidadesPorTiposYFecha(@org.springframework.data.repository.query.Param("tipos") List<MovimientoStock.TipoMovimiento> tipos, @org.springframework.data.repository.query.Param("fecha") java.time.LocalDateTime fecha);
}
