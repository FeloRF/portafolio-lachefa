package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 * Entidad que registar el historial de compras
 * Almacena el detalle de cada producto vendido
 * * @author Felipe Rojas Flores
 * @version 1.0
 * */
@Entity
@Table(name="ventas")
public class Venta {
	
	/** Indentificador de cada transaccion */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**Relacion MUCHOS A UNO (M:N) 
	 * Fetch.lazy mejorar el rendimiento al no cargar el producto al menos que sea necesario
	 */
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name= "producto_id", nullable = false)
private Producto producto;

/** Cantidad de unidades vendidas en esta operacion */
@Column(nullable = false)
private Integer cantidad;

/** * "Snapdhot" del precio: Guardamos el precio al que se vendio.
 * Evita que el historial cambie si el precio del producto sube o baja despues
 */
@Column(nullable = false)
private Integer precioVenta;

/** Registro de tiempo exacto de la operacion */
private LocalDateTime fecha;

/**
 * Constructor por defecto (Requerido por JPA)
 */
public Venta() {
}

/**
 * Constructor Pro: Inicializa la venta con la estampa de tiempo automatica
 */
public Venta(Producto producto, Integer cantidad) {
    this.producto = producto;
    this.cantidad = cantidad;
    this.precioVenta = producto.getPrecio(); // Capturamos el precio actual del producto
    this.fecha = LocalDateTime.now();        // Fecha y hora del sistema
	}

//=================================
// MÉTODO DE  CÁLCULO DINÁMICO
// ================================
/**@return El monto total de esta venta (Precio x Cantidad) */
public Integer getTotalVentas() {
	return this.precioVenta * this.cantidad;
	}

/** @return El monto total de la venta (Preciio * cantidad) */
public Integer getTotalVenta() {
	return this.precioVenta * this.cantidad;
	
// ============================================
// GETTERS Y SETTERS (Acceso Encapsulado)
// ============================================
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
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

public Integer getPrecioVenta() {
	return precioVenta;
}

public void setPrecioVenta(Integer precioVenta) {
	this.precioVenta = precioVenta;
}

public LocalDateTime getFecha() {
	return fecha;
}

public void setFecha(LocalDateTime fecha) {
	this.fecha = fecha;
}



}
	

