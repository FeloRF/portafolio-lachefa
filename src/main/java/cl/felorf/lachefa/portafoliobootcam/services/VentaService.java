package cl.felorf.lachefa.portafoliobootcam.services;

import cl.felorf.lachefa.portafoliobootcam.models.*;
import cl.felorf.lachefa.portafoliobootcam.repositories.VentaRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional // Garantiza que si falla el stock, no se guarda nada
    public Venta generarVenta(List<CarritoItem> itemsCarrito, Usuario cliente, Integer total) {
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setTotal(total); // Seteamos el Integer directamente

        for (CarritoItem item : itemsCarrito) {
            Producto producto = item.getProducto();
            
            // Validación de Stock (Red Team Mindset)
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("No queda stock suficiente de: " + producto.getNombre());
            }

            // Actualización de inventario
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // Creación del detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio()); // Asegúrate que Producto.precio sea Integer
            detalle.setVenta(venta);
            
            // Agregamos a la lista de la venta (JPA Cascade se encarga del resto)
            venta.getDetalles().add(detalle);
        }

        return ventaRepository.save(venta);
    }
}