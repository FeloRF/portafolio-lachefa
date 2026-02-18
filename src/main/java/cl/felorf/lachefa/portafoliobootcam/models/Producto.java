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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El nombre del producto es obligatorio")
	private String nombre;
	
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	
	@Min(0)
	private Integer stock;
	
	private Integer precio;

	/** Estado de visibilidad en el catálogo */
    @Column(nullable = false)
    private boolean activo = true;
    
    /** Nivel de intensidad: 0 (suave) a 5 (extra picante) */
    @Min(0) 
    @Max(5)
    @Column(nullable = false)
    private Integer nivelPicor = 0; 

	// Constructor por defecto
	public Producto() {}
	
	// Constructor Pro
	public Producto(String nombre, Integer stock, Integer precio, String descripcion, Integer nivelPicor) {
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
		this.descripcion = descripcion;
		this.nivelPicor = nivelPicor;
		this.activo = true;
	}

	// GETTERS Y SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

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
}