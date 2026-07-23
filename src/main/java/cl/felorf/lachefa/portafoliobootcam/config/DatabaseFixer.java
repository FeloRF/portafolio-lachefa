package cl.felorf.lachefa.portafoliobootcam.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFixer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> EJECUTANDO PARCHES DE BASE DE DATOS <<<");
        try {
            jdbcTemplate.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_rol_check;");
            System.out.println("Restricción usuarios_rol_check eliminada.");
        } catch (Exception e) {
            System.out.println("No se pudo eliminar usuarios_rol_check: " + e.getMessage());
        }
        
        try {
            jdbcTemplate.execute("ALTER TABLE puntos_venta DROP CONSTRAINT IF EXISTS puntos_venta_estado_check;");
            System.out.println("Restricción puntos_venta_estado_check eliminada.");
        } catch (Exception e) {
            System.out.println("No se pudo eliminar puntos_venta_estado_check: " + e.getMessage());
        }
        
        try {
            jdbcTemplate.execute("ALTER TABLE puntos_venta DROP CONSTRAINT IF EXISTS puntos_venta_tipo_punto_check;");
            System.out.println("Restricción puntos_venta_tipo_punto_check eliminada.");
        } catch (Exception e) {
            System.out.println("No se pudo eliminar puntos_venta_tipo_punto_check: " + e.getMessage());
        }
        
        try {
            jdbcTemplate.execute("ALTER TABLE puntos_venta DROP COLUMN IF EXISTS activo;");
            System.out.println("Columna obsoleta 'activo' eliminada de puntos_venta.");
        } catch (Exception e) {
            System.out.println("No se pudo eliminar columna activo: " + e.getMessage());
        }
    }
}
