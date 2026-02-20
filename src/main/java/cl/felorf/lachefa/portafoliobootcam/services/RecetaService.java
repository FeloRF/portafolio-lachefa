package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Receta;
import cl.felorf.lachefa.portafoliobootcam.repositories.RecetaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    // Requerido por la línea 31 de tu controlador
    public List<Receta> listarTodas() {
        return recetaRepository.findAll();
    }

    // Requerido por la línea 42 de tu controlador
    public Optional<Receta> buscarPorId(Long id) {
        return recetaRepository.findById(id);
    }
}