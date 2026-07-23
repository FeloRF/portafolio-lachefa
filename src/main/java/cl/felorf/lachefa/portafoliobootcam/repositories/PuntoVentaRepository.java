package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.PuntoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoVentaRepository extends JpaRepository<PuntoVenta, Long> {
    
    long countByEstado(PuntoVenta.EstadoPOS estado);
    
    List<PuntoVenta> findByEstadoNot(PuntoVenta.EstadoPOS estado);
    
    List<PuntoVenta> findByEstadoIn(List<PuntoVenta.EstadoPOS> estados);
}
