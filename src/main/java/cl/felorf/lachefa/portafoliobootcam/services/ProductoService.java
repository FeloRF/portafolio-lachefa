package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.repositories.ProductoRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*; // Incluye HashMap, LinkedHashMap, List, etc.

/**
 * Servicio de Negocio: Coordina la persistencia de productos y la gestión de stock.
 * Implementa Inyección por Constructor y lógica de Ranking.
 */
@Service
public class ProductoService {

    // 1. Atributos Finales (Inmutabilidad)
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    // 2. Único Constructor para Inyección (Estándar Senior)
    public ProductoService(ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    // ============================================
    // GESTIÓN DE CONSULTAS Y FILTROS
    // ============================================

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findAll().stream()
                .filter(Producto::isActivo)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> listarInactivos() {
        return productoRepository.findAll().stream()
                .filter(p -> !p.isActivo())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String termino) {
        if (termino != null && !termino.trim().isEmpty()) {
            return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(termino);
        }
        return listarActivos();
    }

    // ============================================
    // ACCIONES DE PERSISTENCIA
    // ============================================

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void borradoLogico(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            productoRepository.save(p);
        });
    }

    @Transactional
    public void restaurar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(true);
            productoRepository.save(p);
        });
    }

    @Transactional
    public void descontarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Producto ID " + id + " no existe."));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Operación cancelada: Stock insuficiente para " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    // ============================================
    // ESTADÍSTICAS E INDICADORES (KPIs)
    // ============================================

    @Transactional(readOnly = true)
    public Integer calcularValorInventario() {
        return listarActivos().stream()
                .mapToInt(p -> (p.getPrecio() != null ? p.getPrecio() : 0) * (p.getStock() != null ? p.getStock() : 0))
                .sum();
    }

    @Transactional(readOnly = true)
    public long contarProductosEnLimiteStock() {
        return listarActivos().stream()
                .filter(p -> p.getStock() <= 5)
                .count();
    }

    /**
     * Procesa el ranking de ventas crudo de la BD y lo convierte en un mapa legible.
     * Mantiene el orden de los más vendidos gracias a LinkedHashMap.
     */
    @Transactional(readOnly = true)
    public Map<Producto, Long> obtenerRankingProductos() {
        List<Object[]> resultados = ventaRepository.obtenerRankingVentas();
        Map<Producto, Long> ranking = new LinkedHashMap<>();

        for (Object[] fila : resultados) {
            Long productoId = (Long) fila[0];
            Long cantidadTotal = (Long) fila[1];
            
            productoRepository.findById(productoId).ifPresent(prod -> {
                ranking.put(prod, cantidadTotal);
            });
        }
        return ranking;
    }
}