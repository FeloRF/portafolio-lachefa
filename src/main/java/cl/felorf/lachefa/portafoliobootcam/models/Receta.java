package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad que representa una receta en el sistema de La Chefa.
 * Incluye metadatos de preparación para mejorar la experiencia del usuario.
 * * @author Felipe Rojas Flores
 * @version 1.1
 */
@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la receta es obligatorio")
    private String nombre;

    /** * Usamos columnDefinition = "TEXT" para que la base de datos soporte 
     * listas largas de ingredientes e instrucciones detalladas.
     */
    @Column(columnDefinition = "TEXT")
    private String ingredientes;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    private String imagenUrl;
    
    private String descripcion;

    // --- NUEVOS CAMPOS PARA EL RECETARIO ---
    
    private String salsa;      // La salsa recomendada para la receta.
    private String dificultad; // Nivel: Fácil, Medio, Experto.
    private String tiempo;     // Tiempo estimado: ej. "30 min".

    /** * Permite al administrador decidir si la receta se muestra 
     * inmediatamente en el catálogo público.
     */
    private boolean publicado = true;

    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    public Receta() {}

    public Receta(String nombre, String ingredientes, String instrucciones, String imagenUrl, 
                  String salsa, String dificultad, String tiempo) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.imagenUrl = imagenUrl;
        this.salsa = salsa;
        this.dificultad = dificultad;
        this.tiempo = tiempo;
        this.publicado = true;
    }

    // ==========================================================
    // GETTERS Y SETTERS
    // ==========================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getSalsa() { return salsa; }
    public void setSalsa(String salsa) { this.salsa = salsa; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getTiempo() { return tiempo; }
    public void setTiempo(String tiempo) { this.tiempo = tiempo; }

    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    
}