package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que registra el historial de compras.
 * Almacena el detalle de cada producto vendido y lo vincula a un cliente si existe.
 * * @author Felipe Rojas Flores
 * @version 1.1
 */
@Entity
@Table(name="ventas")
public class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** * Relación Muchos a Uno: Varias ventas pueden ser del mismo producto.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "producto_id", nullable = false)
	private Producto producto;

	/** * Relación Muchos a Uno: Varias ventas pueden pertenecer a un mismo usuario.
	 * nullable = true permite que sigan existiendo ventas sin registro (invitados).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario cliente;

	@Column(nullable = false)
	private Integer cantidad;

	@Column(nullable = false)
	private Integer precioVenta;

	private LocalDateTime fecha;

	// Constructor por defecto
	public Venta() {
	}

	// Constructor Pro (Venta rápida/Carrito)
	public Venta(Producto producto, Integer cantidad, Usuario cliente) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.cliente = cliente;
		this.precioVenta = producto.getPrecio(); 
		this.fecha = LocalDateTime.now();
	}

	// ============================================
	// MÉTODO DE CÁLCULO DINÁMICO
	// ============================================
	
	/** @return El monto total de la venta (Precio * cantidad) */
	public Integer getTotalVenta() {
		return this.precioVenta * this.cantidad;
	}

	// ============================================
	// GETTERS Y SETTERS
	// ============================================

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Producto getProducto() { return producto; }
	public void setProducto(Producto producto) { this.producto = producto; }

	public Usuario getCliente() { return cliente; }
	public void setCliente(Usuario cliente) { this.cliente = cliente; }

	public Integer getCantidad() { return cantidad; }
	public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

	public Integer getPrecioVenta() { return precioVenta; }
	public void setPrecioVenta(Integer precioVenta) { this.precioVenta = precioVenta; }

	public LocalDateTime getFecha() { return fecha; }
	public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

}