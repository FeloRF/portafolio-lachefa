package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.InventarioPOS;
import cl.felorf.lachefa.portafoliobootcam.models.Producto;
import cl.felorf.lachefa.portafoliobootcam.models.PuntoVenta;
import cl.felorf.lachefa.portafoliobootcam.models.Usuario;
import cl.felorf.lachefa.portafoliobootcam.repositories.InventarioPOSRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.PuntoVentaRepository;
import cl.felorf.lachefa.portafoliobootcam.repositories.UsuarioRepository;
import cl.felorf.lachefa.portafoliobootcam.services.ProductoService;
import cl.felorf.lachefa.portafoliobootcam.services.PuntoVentaService;
import cl.felorf.lachefa.portafoliobootcam.services.UsuarioService;
import cl.felorf.lachefa.portafoliobootcam.models.Rol;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/pos")
public class AdminPOSController {

    private final PuntoVentaService puntoVentaService;
    private final PuntoVentaRepository puntoVentaRepository;
    private final InventarioPOSRepository inventarioPOSRepository;
    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public AdminPOSController(PuntoVentaService puntoVentaService, PuntoVentaRepository puntoVentaRepository,
            InventarioPOSRepository inventarioPOSRepository, ProductoService productoService,
            UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.puntoVentaService = puntoVentaService;
        this.puntoVentaRepository = puntoVentaRepository;
        this.inventarioPOSRepository = inventarioPOSRepository;
        this.productoService = productoService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public String crearPuntoVenta(@ModelAttribute PuntoVenta puntoVenta,
            @RequestParam(name = "vendedorId", required = false) Long vendedorId,
            @RequestParam(name = "asistenteId", required = false) Long asistenteId,
            RedirectAttributes flash) {
        // Asignación explícita de Vendedor Principal
        if (vendedorId != null) {
            usuarioRepository.findById(vendedorId).ifPresent(puntoVenta::setVendedorPrincipal);
        } else {
            puntoVenta.setVendedorPrincipal(null);
        }

        // Asignación explícita de Asistente
        if (asistenteId != null) {
            usuarioRepository.findById(asistenteId).ifPresent(puntoVenta::setAsistente);
        } else {
            puntoVenta.setAsistente(null);
        }

        // Asegurar estado por defecto si no viene
        if (puntoVenta.getEstado() == null) {
            puntoVenta.setEstado(PuntoVenta.EstadoPOS.EN_AGENDA);
        }

        PuntoVenta posGuardado = puntoVentaService.guardar(puntoVenta);
        flash.addFlashAttribute("success", "Punto de Venta '" + posGuardado.getNombre() + "' creado exitosamente.");
        return "redirect:/admin/pos/" + posGuardado.getId() + "/gestion";
    }

    @GetMapping("/{id}/gestion")
    public String detallePOS(@PathVariable Long id, Model model) {
        PuntoVenta pos = puntoVentaRepository.findById(id).orElse(null);
        if (pos == null) {
            return "redirect:/productos/catalogo";
        }

        List<InventarioPOS> inventario = inventarioPOSRepository.findByPuntoVenta(pos);
        // Productos de bodega activos
        List<Producto> productosBodega = productoService.listarActivos();

        model.addAttribute("pos", pos);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productosBodega", productosBodega);
        model.addAttribute("titulo", "Logística POS: " + pos.getNombre());
        return "pos/detalle-logistica";
    }

    @PostMapping("/{id}/despachar")
    public String despacharStock(@PathVariable Long id,
            @RequestParam Long productoId,
            @RequestParam int cantidad,
            Authentication authentication,
            RedirectAttributes flash) {
        try {
            // Se debe obtener el usuario administrador real (aquí se simplifica con
            // búsqueda por email desde Authentication)
            Usuario admin = usuarioRepository.findByEmail(authentication.getName()).orElseThrow();
            puntoVentaService.enviarStockAPOS(id, productoId, cantidad, admin);
            flash.addFlashAttribute("success", "Se despacharon " + cantidad + " unidades correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pos/" + id + "/gestion";
    }

    @PostMapping("/{id}/devolver")
    public String devolverStock(@PathVariable Long id,
            @RequestParam Long productoId,
            @RequestParam int cantidad,
            Authentication authentication,
            RedirectAttributes flash) {
        try {
            Usuario admin = usuarioRepository.findByEmail(authentication.getName()).orElseThrow();
            puntoVentaService.devolverStockABodega(id, productoId, cantidad, admin);
            flash.addFlashAttribute("success", "Se devolvieron " + cantidad + " unidades a la bodega central.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pos/" + id + "/gestion";
    }

    @PostMapping("/vendedor/crear")
    public String registrarVendedor(@RequestParam String nombreCompleto,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes flash) {
        try {
            Usuario nuevoVendedor = new Usuario();
            nuevoVendedor.setNombreCompleto(nombreCompleto);
            nuevoVendedor.setEmail(email);
            nuevoVendedor.setPassword(password);
            nuevoVendedor.setRol(Rol.VENDEDOR);
            usuarioService.registrar(nuevoVendedor);
            flash.addFlashAttribute("success", "Vendedor " + nombreCompleto + " registrado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al registrar vendedor: " + e.getMessage());
        }
        return "redirect:/productos/catalogo";
    }

    @PostMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Long id,
            @RequestParam("nuevoEstado") PuntoVenta.EstadoPOS nuevoEstado,
            @RequestHeader(value = "Referer", required = false) String referer,
            RedirectAttributes flash) {
        puntoVentaRepository.findById(id).ifPresent(pos -> {
            pos.setEstado(nuevoEstado);
            puntoVentaRepository.save(pos);
            flash.addFlashAttribute("success", "Estado del Punto de Venta '" + pos.getNombre() + "' actualizado a " + nuevoEstado + ".");
        });
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/productos/catalogo";
    }
}
