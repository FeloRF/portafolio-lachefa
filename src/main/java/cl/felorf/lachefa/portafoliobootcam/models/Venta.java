package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList; // IMPORTANTE: Faltaba este
import java.util.List;      // IMPORTANTE: Faltaba este

/**
 * Entidad que registra el encabezado de una venta.
 * Vincula al cliente con el total de la compra y su desglose de productos.
 */
@Entity
@Table(name="ventas")
public class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario cliente;
	
	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleVenta> detalles = new ArrayList<>();

	private Integer total; // El monto final de la compra

	private LocalDateTime fecha = LocalDateTime.now();

	// ============================================
	// CONSTRUCTORES
	// ============================================
	
	public Venta() {
	}

	// ============================================
	// GETTERS Y SETTERS (Para el CheckoutController)
	// ============================================

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Usuario getCliente() { return cliente; }
	public void setCliente(Usuario cliente) { this.cliente = cliente; }

	public List<DetalleVenta> getDetalles() { return detalles; }
	public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }

	public Integer getTotal() { return total; }
	public void setTotal(Integer total) { this.total = total; }

	public LocalDateTime getFecha() { return fecha; }
	public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}