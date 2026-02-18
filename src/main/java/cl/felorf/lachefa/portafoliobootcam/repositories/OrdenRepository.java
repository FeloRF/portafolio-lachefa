package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de Ordenes de compra.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    // Aquí podrías agregar búsquedas por emailInvitado o por cliente_id
}