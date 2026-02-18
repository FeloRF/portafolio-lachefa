
package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la cabecera de un pedido realizado.
 * Almacena la información del comprador (Usuario o Invitado) y el total de la transacción.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora exacta en la que se confirmó el pago */
    private LocalDateTime fecha;

    /** Monto total consolidado de la orden */
    private Integer total;

    /** * Relación con el usuario registrado. 
     * Si este campo es null, la orden pertenece a un cliente invitado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario cliente;

    /** Nombre proporcionado por el cliente si compra como invitado */
    private String nombreInvitado;

    /** Correo de contacto para envío de comprobante si compra como invitado */
    private String emailInvitado;

    /** * Lista de productos y cantidades asociados a esta orden.
     * El uso de CascadeType.ALL asegura que al guardar la orden, se guarden sus detalles automáticamente.
     */
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();

    /**
     * Constructor por defecto. Inicializa la estampa de tiempo.
     */
    public Orden() {
        this.fecha = LocalDateTime.now();
        
     // Getters y Setters...
        
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

	public Usuario getCliente() {
		return cliente;
	}
	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public String getNombreInvitado() {
		return nombreInvitado;
	}
	public void setNombreInvitado(String nombreInvitado) {
		this.nombreInvitado = nombreInvitado;
	}

	public String getEmailInvitado() {
		return emailInvitado;
	}
	public void setEmailInvitado(String emailInvitado) {
		this.emailInvitado = emailInvitado;
	}

	public List<DetalleOrden> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleOrden> detalles) {
		this.detalles = detalles;
	}

    
    
    
}