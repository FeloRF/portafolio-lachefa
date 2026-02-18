package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;

/**
 * Representa cada línea de producto dentro de una orden de compra.
 * Captura el precio del producto al momento de la venta para integridad histórica.
 * * @author Felipe Rojas Flores
 */
@Entity
@Table(name = "detalles_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id")
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    /** Precio unitario al que se vendió el producto (Snapshot financiero) */
    private Integer precioUnitario;

    public DetalleOrden() {}

    /**
     * Calcula el subtotal de esta línea de pedido.
     * @return El resultado de precio unitario por cantidad.
     */
    public Integer getSubtotal() {
        return precioUnitario * cantidad;
        
     // Getters y Setters...
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Orden getOrden() {
		return orden;
	}
	public void setOrden(Orden orden) {
		this.orden = orden;
	}

	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Integer getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Integer precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

    
    
    
}