package cl.felorf.lachefa.portafoliobootcam.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad que reprecenta a los productos en el inventario.
 * clase mapeada a la tabla 'productos' de a BD
 * * @author Felipe Rojas Flores
 * @version 1.0
 * */

@Entity
@Table(name = "productos")
public class Producto {
	
	/** Identificador autoincremental en la BD */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** Nombre del producto. NOT NULL */
	@NotBlank(message = "El nombre del producto es obligatorio")
	private String nombre;
	
	/** Descripcion del producto y nivel de picante */
	private String descripcion;
	
	/** Cantidad disponible en bodega, mayor a 0 */
	@Min(0)
	private Integer stock;
	
	/** Precio de venta */
	private Integer precio;
	
	// Agrega esto en Producto.java
    private boolean activo = true; // Por defecto todos nacen activos
	
	/**
	 * Constructor por defecto requerido por JPA.
	 * */
	public Producto() {}
	
	/**
     * Constructor para inicializar un producto con datos esenciales.
     * * @param nombre Nombre del producto.
     * @param stock Cantidad inicial.
     * @param precio Valor monetario.
     */
	public Producto(String nombre, Integer stock, Integer precio) {
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
	}

	/** @return El ID único del producto. */
    public Long getId() { return id; }
    
    /** @param id El nuevo ID para el producto. */
    public void setId(Long id) { this.id = id; }

    /** @return Nombre del producto. */
    public String getNombre() { return nombre; }
    
    /** @param nombre El nombre a asignar. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return Cantidad actual en stock. */
    public Integer getStock() { return stock; }
    
    /** @param stock La nueva cantidad disponible. */
    public void setStock(Integer stock) { this.stock = stock; }

    /** @return Precio unitario. */
    public Integer getPrecio() { return precio; }
    
    /** @param precio El precio . */
    public void setPrecio(Integer precio) { this.precio = precio; }
    
    /** @return Descripcion del producto. */
    public String getDescripcion() { 
        return descripcion; 
    }
    
    /** @param descripcion La descripcion a asignar. */
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
    
    

    //  Getter y Setter (o Eclipse te dará error 500 de nuevo)
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}