package cl.felorf.lachefa.portafoliobootcam.controller;

import cl.felorf.lachefa.portafoliobootcam.models.*;
import cl.felorf.lachefa.portafoliobootcam.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/pos")
public class AdminPOSDashboardController {

    private final PuntoVentaRepository puntoVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final ProductoRepository productoRepository;

    public AdminPOSDashboardController(PuntoVentaRepository puntoVentaRepository,
                                       UsuarioRepository usuarioRepository,
                                       VentaRepository ventaRepository,
                                       MovimientoStockRepository movimientoStockRepository,
                                       ProductoRepository productoRepository) {
        this.puntoVentaRepository = puntoVentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaRepository = ventaRepository;
        this.movimientoStockRepository = movimientoStockRepository;
        this.productoRepository = productoRepository;
    }

    private LocalDateTime obtenerInicioJornada() {
        LocalDateTime ahora = LocalDateTime.now();
        // Jornada empieza a las 06:00 AM
        if (ahora.getHour() < 6) {
            return ahora.minusDays(1).withHour(6).withMinute(0).withSecond(0).withNano(0);
        }
        return ahora.withHour(6).withMinute(0).withSecond(0).withNano(0);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDateTime inicioJornada = obtenerInicioJornada();

        // kpiPosActivos y listaPos
        List<PuntoVenta> listaPos = puntoVentaRepository.findAll();
        long kpiPosActivos = listaPos.stream().filter(p -> p.getEstado() == PuntoVenta.EstadoPOS.ACTIVO).count();

        // Ventas de la jornada
        List<Venta> ventasJornada = ventaRepository.buscarVentasRecientes(inicioJornada);
        double kpiVentaTotal = ventasJornada.stream().mapToDouble(Venta::getTotal).sum();
        double kpiTicketPromedioGlobal = ventasJornada.isEmpty() ? 0 : kpiVentaTotal / ventasJornada.size();

        // Mermas y devoluciones
        List<MovimientoStock.TipoMovimiento> tiposMerma = Arrays.asList(
                MovimientoStock.TipoMovimiento.MERMA, 
                MovimientoStock.TipoMovimiento.DEVOLUCION_BODEGA, 
                MovimientoStock.TipoMovimiento.DEVOLUCION_CLIENTE
        );
        Integer kpiMermas = movimientoStockRepository.sumarCantidadesPorTiposYFecha(tiposMerma, inicioJornada);
        if (kpiMermas == null) kpiMermas = 0;

        // Ranking Vendedores (por recaudación en la jornada)
        Map<Usuario, Double> ventasPorVendedor = new HashMap<>();
        Map<Usuario, Integer> transaccionesPorVendedor = new HashMap<>();
        List<Usuario> todosVendedores = usuarioRepository.findByRol(Rol.VENDEDOR);
        for (Usuario v : todosVendedores) {
            ventasPorVendedor.put(v, 0.0);
            transaccionesPorVendedor.put(v, 0);
        }
        for (Venta v : ventasJornada) {
            Usuario vendedor = v.getVendedor();
            if (vendedor != null && vendedor.getRol() == Rol.VENDEDOR) {
                ventasPorVendedor.put(vendedor, ventasPorVendedor.getOrDefault(vendedor, 0.0) + v.getTotal());
                transaccionesPorVendedor.put(vendedor, transaccionesPorVendedor.getOrDefault(vendedor, 0) + 1);
            }
        }
        // Ordenar de mayor a menor
        List<Map.Entry<Usuario, Double>> rankingVendedores = ventasPorVendedor.entrySet().stream()
                .sorted(Map.Entry.<Usuario, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        // Ranking Salsas (Top 3) usando ventasJornada
        Map<Long, Integer> cantPorProducto = new HashMap<>();
        for (Venta v : ventasJornada) {
            for (DetalleVenta d : v.getDetalles()) {
                long prodId = d.getProducto().getId();
                cantPorProducto.put(prodId, cantPorProducto.getOrDefault(prodId, 0) + d.getCantidad());
            }
        }
        List<Map.Entry<Long, Integer>> top3ProductosIds = cantPorProducto.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());
        
        List<Map<String, Object>> rankingSalsas = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : top3ProductosIds) {
            Producto p = productoRepository.findById(entry.getKey()).orElse(null);
            if (p != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("producto", p);
                map.put("cantidad", entry.getValue());
                rankingSalsas.add(map);
            }
        }

        model.addAttribute("kpiVentaTotal", kpiVentaTotal);
        model.addAttribute("kpiPosActivos", kpiPosActivos);
        model.addAttribute("kpiMermas", kpiMermas);
        model.addAttribute("kpiTicketPromedioGlobal", kpiTicketPromedioGlobal);
        
        model.addAttribute("listaPos", listaPos);
        model.addAttribute("rankingVendedores", rankingVendedores);
        model.addAttribute("transaccionesPorVendedor", transaccionesPorVendedor);
        model.addAttribute("rankingSalsas", rankingSalsas);

        return "pos/dashboard";
    }
}
