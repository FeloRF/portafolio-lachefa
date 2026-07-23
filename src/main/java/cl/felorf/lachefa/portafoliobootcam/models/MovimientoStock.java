package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que registra los movimientos de stock entre Bodega y Puntos de Venta.
 */
@Entity
@Table(name = "movimientos_stock")
public class MovimientoStock {

    public enum TipoMovimiento {
        DESPACHO,
        VENTA,
        DEVOLUCION_BODEGA,
        MERMA,
        DEVOLUCION_CLIENTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_venta_id", nullable = false)
    private PuntoVenta puntoVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Usuario adminResponsable;

    // ============================================
    // CONSTRUCTORES
    // ============================================

    public MovimientoStock() {
    }

    public MovimientoStock(PuntoVenta puntoVenta, Producto producto, TipoMovimiento tipo, Integer cantidad, Usuario adminResponsable) {
        this.puntoVenta = puntoVenta;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.adminResponsable = adminResponsable;
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

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Usuario getAdminResponsable() { return adminResponsable; }
    public void setAdminResponsable(Usuario adminResponsable) { this.adminResponsable = adminResponsable; }
}
