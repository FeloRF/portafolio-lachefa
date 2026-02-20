package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;

@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String salsa;
    private String dificultad;
    private String tiempo;
    
    @Column(length = 500)
    private String descripcion;

    @Lob 
    private String ingredientes;

    @Lob 
    private String instrucciones;

    // Constructor vacío (Obligatorio para JPA)
    public Receta() {}

    // --- GETTERS Y SETTERS (Esto es lo que soluciona el error de la línea 50) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; } // <--- EL MÉTODO QUE FALTABA
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSalsa() { return salsa; }
    public void setSalsa(String salsa) { this.salsa = salsa; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getTiempo() { return tiempo; }
    public void setTiempo(String tiempo) { this.tiempo = tiempo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}