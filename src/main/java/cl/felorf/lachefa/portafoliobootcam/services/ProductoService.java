package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.Venta;
import cl.felorf.lachefa.portafoliobootcam.repositories.ProductoRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio de Negocio: Coordina la persistencia de productos y el registro de ventas.
 * Implementa @Transactional para asegurar la integridad de los datos en operaciones complejas.
 */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private VentaRepository ventaRepository; //inyección del repositorio de ventas

    /**
     * Recupera todos los productos sin filtrar (Uso administrativo).
     */
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Borrado Lógico: Cambia el estado del producto a inactivo.
     * @param id Identificador del producto.
     */
    @Transactional
    public void borradoLogico(Long id) {
        Producto p = productoRepository.findById(id).orElse(null);
        if (p != null) {
            p.setActivo(false);
            productoRepository.save(p);
        }
    }

    /**
     * Filtra productos activos por coincidencia de nombre.
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String termino) {
        if (termino != null && !termino.trim().isEmpty()) {
            return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(termino);
        }
        return listarActivos();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findAll().stream()
                .filter(p -> p.isActivo())
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> listarInactivos() {
        // Usamos el repositorio para traer los que tienen activo = false
        return productoRepository.findAll().stream()
                .filter(p -> !p.isActivo())
                .toList();
    }

    @Transactional
    public void restaurar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(true);
            productoRepository.save(p);
        });
    }

    /**
     * Proceso de Venta: Descuenta stock, genera un registro histórico de forma atómica y crea un registro de los productos 
     * mas vendidos y menos vendidos.
     * Si el descuento de stock o el guardado de la venta falla, la base de datos hace rollback.
     * * @param id Identificador del producto.
     * @param cantidad Unidades vendidas.
     */
    @Transactional
    public void descontarStock(Long id, Integer cantidad) {
        // 1. Buscamos el producto en la BD
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Producto ID " + id + " no existe."));

        // 2. Validación de regla de negocio: ¿Tenemos suficiente para vender?
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Operación cancelada: Stock insuficiente para " + producto.getNombre());
        }

        // 3. Actualizamos el stock del producto
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        // 4. CREAMOS EL REGISTRO DE VENTA (La huella digital de la transacción)
        // Usamos el constructor Pro que creamos antes que captura precio y fecha automáticamente.
        Venta nuevaVenta = new Venta(producto, cantidad, null);
        ventaRepository.save(nuevaVenta);
        
        // Al terminar el método, Spring hace el "Commit" de ambas operaciones.
    }
    
    /**
     * Analiza el historial de ventas para identificar los productos más y menos populares.
     * @return Un objeto (Map) con los IDs destacados.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerExtremosVentas() {
        List<Object[]> ranking = ventaRepository.obtenerRankingVentas();
        Map<String, Long> resultados = new HashMap<>();

        // Si hay datos, los ponemos. Si no, el Map queda vacío pero NO nulo.
        if (ranking != null && !ranking.isEmpty()) {
            resultados.put("masVendido", (Long) ranking.get(0)[0]);
            resultados.put("menosVendido", (Long) ranking.get(ranking.size() - 1)[0]);
        }
        return resultados;
    }
    
    /**
     * Calcula el valor monetario total del inventario activo.
     * @return Suma de (precio * stock) de todos los productos vigentes.
     */
    @Transactional(readOnly = true)
    public Integer calcularValorInventario() {
        return listarActivos().stream()
                .mapToInt(p -> p.getPrecio() * p.getStock())
                .sum();
    }
    
    /**
     * Cuenta cuántos productos activos tienen un stock por debajo del límite crítico (5 unidades).
     * @return Cantidad de productos en riesgo.
     */
    @Transactional(readOnly = true)
    public long contarProductosEnLimiteStock() {
        return listarActivos().stream()
                .filter(p -> p.getStock() <= 5)
                .count();
    }
    
    
}