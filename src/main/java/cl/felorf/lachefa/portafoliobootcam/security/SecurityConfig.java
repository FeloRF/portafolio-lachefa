package cl.felorf.lachefa.portafoliobootcam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            // 1. ZONA PÚBLICA: Usamos wildcards (**) para evitar el error 403 por barras extras
	            .requestMatchers("/", "/home", "/login", "/registro", "/css/**", "/js/**", "/img/**").permitAll()
	            .requestMatchers("/catalogoLaChefa/**", "/recetario").permitAll() // Público
	            
	            // 2. ZONA ADMINISTRATIVA: Blindamos específicamente la gestión
	            .requestMatchers("/productos/**", "/ventas/**", "/nueva-receta").hasAuthority("ADMIN")
	            
	            // 3. ZONA CLIENTE: El resto requiere autenticación
	            .anyRequest().authenticated()
	        )
	        .exceptionHandling(exception -> exception.accessDeniedPage("/")) 
	        .formLogin(form -> form
	            .loginPage("/login") 
	            .usernameParameter("email")
	            .defaultSuccessUrl("/", true) 
	            .permitAll()
	        )
	        .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
	        .csrf(csrf -> csrf.disable());
	        
	    return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}