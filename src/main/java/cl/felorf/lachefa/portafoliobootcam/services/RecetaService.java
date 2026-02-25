package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.repositories.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para seguridad de datos
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Transactional(readOnly = true)
    public List<Receta> listarTodas() {
        return recetaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Receta> buscarPorId(Long id) {
        return recetaRepository.findById(id);
    }

    /**
     * Guarda o actualiza una receta. 
     * @Transactional asegura que si algo falla, no se guarde nada a medias.
     */
    @Transactional
    public void guardar(Receta receta) {
        recetaRepository.save(receta);
    }

    @Transactional
    public void eliminar(Long id) {
        recetaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return recetaRepository.existsById(id);
    }
}