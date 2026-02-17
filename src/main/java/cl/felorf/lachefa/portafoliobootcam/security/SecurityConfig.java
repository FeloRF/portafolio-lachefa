package cl.felorf.lachefa.portafoliobootcam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración temporal para desactivar las restricciones de seguridad.
 * PERMITIRÁ EL ACCESO TOTAL sin pedir login.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // LIBERTAD TOTAL
            )
            .csrf(csrf -> csrf.disable()) // Deshabilita protección contra ataques CSRF (Segurizar al final)
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); 
            
        return http.build();
    }
}