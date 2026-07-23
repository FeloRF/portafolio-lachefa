package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.InventarioPOS;
import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.PuntoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioPOSRepository extends JpaRepository<InventarioPOS, Long> {
    
    List<InventarioPOS> findByPuntoVenta(PuntoVenta puntoVenta);
    
    Optional<InventarioPOS> findByPuntoVentaAndProducto(PuntoVenta puntoVenta, Producto producto);
}
