package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

/**
 * Entidad que registra el stock actual de un producto en un Punto de Venta específico.
 */
@Entity
@Table(name = "inventarios_pos")
public class InventarioPOS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_venta_id", nullable = false)
    private PuntoVenta puntoVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Min(0)
    @Column(nullable = false)
    private Integer stockActual = 0;

    // ============================================
    // CONSTRUCTORES
    // ============================================

    public InventarioPOS() {
    }

    public InventarioPOS(PuntoVenta puntoVenta, Producto producto, Integer stockActual) {
        this.puntoVenta = puntoVenta;
        this.producto = producto;
        this.stockActual = stockActual;
    }

    // ============================================
    // GETTERS Y SETTERS
    // ============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PuntoVenta getPuntoVenta() { return puntoVenta; }
    public void setPuntoVenta(PuntoVenta puntoVenta) { this.puntoVenta = puntoVenta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}
