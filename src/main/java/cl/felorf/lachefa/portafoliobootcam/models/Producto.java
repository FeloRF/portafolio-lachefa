package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad que representa a los productos en el inventario.
 * Se han corregido los errores de sintaxis en los getters y consolidado los campos.
 * @author Felipe Rojas Flores
 * @version 2.0
 */
@Entity
@Table(name = "productos")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El nombre del producto es obligatorio")
	private String nombre;
	
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	
	@Min(0)
	private Integer stock = 0;
	
	@Min(0)
	private Integer precio = 0;
	
	// Campo vital para el catálogo y ranking
	private String categoria;
	
	private String imagenUrl;

    /** Estado de visibilidad en el catálogo */
    @Column(nullable = false)
    private boolean activo = true;
    
    /** Nivel de intensidad: 0 (suave) a 5 (extra picante) */
    @Min(0) 
    @Max(5)
    @Column(nullable = false)
    private Integer nivelPicor = 0; 

	// Constructor por defecto obligatorio para JPA
	public Producto() {}
	
	// Constructor Pro
	public Producto(String nombre, Integer stock, Integer precio, String descripcion, Integer nivelPicor, String categoria) {
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
		this.descripcion = descripcion;
		this.nivelPicor = nivelPicor;
		this.categoria = categoria;
		this.activo = true;
	}

	// ============================================
	// GETTERS Y SETTERS (Ordenados y Limpios)
	// ============================================

    public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Integer getNivelPicor() { return nivelPicor; }
    public void setNivelPicor(Integer nivelPicor) { this.nivelPicor = nivelPicor; }
    
    public String getImagenUrl() {
        return imagenUrl;}
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}