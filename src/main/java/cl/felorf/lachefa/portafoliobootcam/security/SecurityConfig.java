package cl.felorf.lachefa.portafoliobootcam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // RUTAS PÚBLICAS: Todo el mundo puede ver la tienda y el inicio
                .requestMatchers("/", "/catalogoLaChefa", "/carrito/**", "/recetario", "/login", "/css/**", "/js/**", "/img/**").permitAll() 
                
                // RUTAS PROTEGIDAS: Solo el Admin entra a inventario y ventas
                .requestMatchers("/productos/**", "/ventas/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Nuestra página personalizada
                .defaultSuccessUrl("/productos/catalogo", true) // A donde va el admin al loguearse
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // Al salir, vuelve al home
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Deshabilitado para facilitar pruebas de formularios
            
        return http.build();
    }

    /**
     * USUARIO DE PRUEBA: 
     * Creamos un admin en memoria para que puedas probar el flujo de inmediato.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("chefa2026")
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }
}