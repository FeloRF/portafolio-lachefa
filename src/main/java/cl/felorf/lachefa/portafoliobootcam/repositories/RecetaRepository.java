package cl.felorf.lachefa.portafoliobootcam.repositories;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
}