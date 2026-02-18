package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Entidad que representa a los actores del sistema (Administradores y Clientes).
 * Almacena información de perfil y vincula el historial de compras.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank(message = "El nombre es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /** Definición del rol para seguridad y lógica de negocio */
    @Enumerated(EnumType.STRING)
    private Rol rol;

    /** * Relación Uno a Muchos: Un usuario puede tener múltiples registros de compra.
     * mappedBy indica que el dueño de la relación es el campo 'cliente' en la clase Venta.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> compras;

    // Constructor vacío (JPA)
    public Usuario() {}

    // Constructor para registro rápido
    public Usuario(String nombreCompleto, String email, String password, Rol rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // ==========================================================
    // GETTERS Y SETTERS
    // ==========================================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public List<Venta> getCompras() { return compras; }
    public void setCompras(List<Venta> compras) { this.compras = compras; }
}


