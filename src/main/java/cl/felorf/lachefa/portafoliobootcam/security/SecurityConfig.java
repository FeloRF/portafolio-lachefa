package cl.felorf.lachefa.portafoliobootcam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 1. RUTAS PÚBLICAS
                .requestMatchers("/", "/catalogoLaChefa", "/recetario", "/login", "/registro", 
                                "/carrito/**", "/checkout/**", "/exito/**", 
                                "/css/**", "/js/**", "/img/**").permitAll() 
                
                // 2. EL MURO DEL RECETARIO (Detalles protegidos)
                .requestMatchers("/recetario/detalle/**").hasAnyRole("USER", "ADMIN")
                
                // 3. ZONA PRIVADA DEL CLIENTE
                .requestMatchers("/perfil/**", "/favoritos/**").hasAnyRole("USER", "ADMIN")
                
                // 4. RUTAS DE ADMINISTRACIÓN
                .requestMatchers("/productos/**", "/ventas/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/", false) // false permite que si venía de una receta, vuelva a ella
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") 
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); 
            
        return http.build();
    }

    /**
     * USUARIOS EN MEMORIA: 
     * Ahora usamos explícitamente el passwordEncoder para las contraseñas hardcoded.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();

        // Admin encriptado
        UserDetails admin = User.builder()
            .username("admin")
            .password(encoder.encode("chefa2026")) // Usamos el encoder aquí
            .roles("ADMIN")
            .build();

        // Cliente encriptado
        UserDetails cliente = User.builder()
            .username("felo")
            .password(encoder.encode("salsa2026")) // Usamos el encoder aquí
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, cliente);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Motor de encriptación oficial
    }
}