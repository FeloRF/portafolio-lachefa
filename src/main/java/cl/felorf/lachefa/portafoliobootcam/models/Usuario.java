package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Entidad que representa a los actores del sistema.
 * Soporta tres estados: Administrador, Cliente Registrado y Cliente Invitado.
 * * @author Felipe Rojas Flores
 * @version 2.1
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
    
    /** * Nombre de usuario único (ej: 'admin'). 
     * Permite login sin necesidad de usar el email para administradores.
     */
    @Column(unique = true)
    private String username;

    /** * La contraseña ya no es @NotBlank para permitir clientes invitados.
     * Se valida manualmente en el proceso de registro.
     */
    private String password;

    /** Campo para despacho, esencial para la logística */
    private String direccion;

    /** Definición del rol: ADMIN o CLIENTE */
    @Enumerated(EnumType.STRING)
    private Rol rol;

    /** Relación con el historial de compras */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> compras;

    // ==========================================================
    // CONSTRUCTORES
    // ==========================================================

    public Usuario() {}

    /** * Constructor para Clientes Invitados.
     * Se usa el email como username temporal por defecto.
     */
    public Usuario(String nombreCompleto, String email, String direccion) {
        this.nombreCompleto = nombreCompleto;
        this.email = email.toLowerCase().trim();
        this.username = email.toLowerCase().trim();
        this.direccion = direccion;
        this.rol = Rol.CLIENTE;
    }

    /** * Constructor completo para Administradores o Clientes con cuenta.
     * Incluye el alias (username) para el login híbrido.
     */
    public Usuario(String nombreCompleto, String email, String username, String password, Rol rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email.toLowerCase().trim();
        this.username = (username != null) ? username.trim() : email.toLowerCase().trim();
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

    /** Getter y Setter para Username (Vital para login híbrido) */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public List<Venta> getCompras() { return compras; }
    public void setCompras(List<Venta> compras) { this.compras = compras; }
}