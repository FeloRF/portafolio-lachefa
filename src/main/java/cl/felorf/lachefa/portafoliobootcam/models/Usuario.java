package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Entidad que representa a los actores del sistema.
 * Soporta tres estados: Administrador, Cliente Registrado y Cliente Invitado.
 * * @author Felipe Rojas Flores
 * @version 2.0
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

    /** * La contraseña ya no es @NotBlank para permitir clientes invitados.
     * Se validará manualmente solo cuando el usuario decida registrarse.
     */
    private String password;

    /**Campo para despacho, esencial para clientes invitados y registrados */
    private String direccion;

    /** Definición del rol: ADMIN o CLIENTE */
    @Enumerated(EnumType.STRING)
    private Rol rol;

    /** Relación con el historial de compras */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> compras;

    
    // CONSTRUCTORES    

    public Usuario() {}

    /** Constructor para Clientes Invitados (Sin contraseña) */
    public Usuario(String nombreCompleto, String email, String direccion) {
        this.nombreCompleto = nombreCompleto;
        this.email = email.toLowerCase();
        this.direccion = direccion;
        this.rol = Rol.CLIENTE;
    }

    /** Constructor para Registro de Admin o Cliente con cuenta */
    public Usuario(String nombreCompleto, String email, String password, Rol rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email.toLowerCase();
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

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public List<Venta> getCompras() { return compras; }
    public void setCompras(List<Venta> compras) { this.compras = compras; }
}