package cl.felorf.lachefa.portafoliobootcam.repositories; // Usamos .repository en minúsculas

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    // Aquí heredamos automáticamente findAll() y findById()
}