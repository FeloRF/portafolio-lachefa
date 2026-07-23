package cl.felorf.lachefa.portafoliobootcam.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un Punto de Venta (POS) físico.
 */
@Entity
@Table(name = "puntos_venta")
public class PuntoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum EstadoPOS {
        ACTIVO,
        EN_AGENDA,
        CERRADO,
        FINALIZADO
    }

    public enum TipoPunto {
        PERMANENTE,
        EVENTO_TEMPORAL
    }

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;
    
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPOS estado = EstadoPOS.EN_AGENDA;
    
    @Column(length = 500)
    private String urlMapa;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vendedor_principal_id")
    private Usuario vendedorPrincipal;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "asistente_id")
    private Usuario asistente;
    
    private String nombreResponsableTurno;
    
    // Métricas CRO y Operativas (Ticket 4 / UI Form)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPunto tipoPunto = TipoPunto.EVENTO_TEMPORAL;
    
    private java.time.LocalDate fechaFin;
    
    private Double fondoCajaInicial = 0.0;
    
    private Double metaVentas = 0.0;
    
    private Integer umbralStockCritico = 5;
    
    private String utmCampana;

    // ============================================
    // CONSTRUCTORES
    // ============================================

    public PuntoVenta() {
    }

    public PuntoVenta(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // ============================================
    // GETTERS Y SETTERS
    // ============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public EstadoPOS getEstado() { return estado; }
    public void setEstado(EstadoPOS estado) { this.estado = estado; }

    public Usuario getVendedorPrincipal() { return vendedorPrincipal; }
    public void setVendedorPrincipal(Usuario vendedorPrincipal) { this.vendedorPrincipal = vendedorPrincipal; }

    public Usuario getAsistente() { return asistente; }
    public void setAsistente(Usuario asistente) { this.asistente = asistente; }

    public String getNombreResponsableTurno() { return nombreResponsableTurno; }
    public void setNombreResponsableTurno(String nombreResponsableTurno) { this.nombreResponsableTurno = nombreResponsableTurno; }

    public TipoPunto getTipoPunto() { return tipoPunto; }
    public void setTipoPunto(TipoPunto tipoPunto) { this.tipoPunto = tipoPunto; }

    public java.time.LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(java.time.LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getFondoCajaInicial() { return fondoCajaInicial; }
    public void setFondoCajaInicial(Double fondoCajaInicial) { this.fondoCajaInicial = fondoCajaInicial; }

    public Double getMetaVentas() { return metaVentas; }
    public void setMetaVentas(Double metaVentas) { this.metaVentas = metaVentas; }

    public Integer getUmbralStockCritico() { return umbralStockCritico; }
    public void setUmbralStockCritico(Integer umbralStockCritico) { this.umbralStockCritico = umbralStockCritico; }

    public String getUtmCampana() { return utmCampana; }
    public void setUtmCampana(String utmCampana) { this.utmCampana = utmCampana; }

    public String getUrlMapa() { return urlMapa; }
    public void setUrlMapa(String urlMapa) { this.urlMapa = urlMapa; }
}
