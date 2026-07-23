package cl.felorf.lachefa.portafoliobootcam.models;

/**
 * Define los niveles de acceso dentro del ecosistema de La Chefa.
 * * @author Felipe Rojas Flores
 * @version 1.0
 */
public enum Rol {
	ADMIN, // Dueño/Gestor: Acceso a CRUD y Ventas.
	CLIENTE, // Comprador: Acceso a Carrito e Historial personal.
	VENDEDOR // Vendedor de POS físico
}
