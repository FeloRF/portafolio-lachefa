package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.InventarioPOS;
import cl.felorf.lachefa.portafoliobootcam.models.MovimientoStock;
import cl.felorf.lachefa.portafoliobootcam.models.MovimientoStock.TipoMovimiento;
import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.PuntoVenta;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.InventarioPOSRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.MovimientoStockRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.ProductoRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.PuntoVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PuntoVentaService {

    private final PuntoVentaRepository puntoVentaRepository;
    private final InventarioPOSRepository inventarioPOSRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    public PuntoVentaService(PuntoVentaRepository puntoVentaRepository, InventarioPOSRepository inventarioPOSRepository,
                             ProductoRepository productoRepository, MovimientoStockRepository movimientoStockRepository) {
        this.puntoVentaRepository = puntoVentaRepository;
        this.inventarioPOSRepository = inventarioPOSRepository;
        this.productoRepository = productoRepository;
        this.movimientoStockRepository = movimientoStockRepository;
    }

    public PuntoVenta guardar(PuntoVenta puntoVenta) {
        return puntoVentaRepository.save(puntoVenta);
    }

    public Optional<PuntoVenta> buscarPorId(Long id) {
        return puntoVentaRepository.findById(id);
    }

    @Transactional
    public void enviarStockAPOS(Long posId, Long productoId, int cantidad, Usuario admin) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");

        PuntoVenta pos = puntoVentaRepository.findById(posId)
                .orElseThrow(() -> new IllegalArgumentException("Punto de venta no encontrado"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente en Bodega Central (disponible: " + producto.getStock() + ")");
        }

        // Descontar de bodega central
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        // Sumar al POS
        InventarioPOS inventarioPOS = inventarioPOSRepository.findByPuntoVentaAndProducto(pos, producto)
                .orElse(new InventarioPOS(pos, producto, 0));
        inventarioPOS.setStockActual(inventarioPOS.getStockActual() + cantidad);
        inventarioPOSRepository.save(inventarioPOS);

        // Registrar movimiento
        MovimientoStock movimiento = new MovimientoStock(pos, producto, TipoMovimiento.DESPACHO, cantidad, admin);
        movimientoStockRepository.save(movimiento);
    }

    @Transactional
    public void devolverStockABodega(Long posId, Long productoId, int cantidad, Usuario admin) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");

        PuntoVenta pos = puntoVentaRepository.findById(posId)
                .orElseThrow(() -> new IllegalArgumentException("Punto de venta no encontrado"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        InventarioPOS inventarioPOS = inventarioPOSRepository.findByPuntoVentaAndProducto(pos, producto)
                .orElseThrow(() -> new IllegalArgumentException("El producto no está en el inventario del POS"));

        if (inventarioPOS.getStockActual() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente en el POS (disponible: " + inventarioPOS.getStockActual() + ")");
        }

        // Descontar del POS
        inventarioPOS.setStockActual(inventarioPOS.getStockActual() - cantidad);
        inventarioPOSRepository.save(inventarioPOS);

        // Devolver a bodega central
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);

        // Registrar movimiento
        MovimientoStock movimiento = new MovimientoStock(pos, producto, TipoMovimiento.DEVOLUCION_BODEGA, cantidad, admin);
        movimientoStockRepository.save(movimiento);
    }
}
