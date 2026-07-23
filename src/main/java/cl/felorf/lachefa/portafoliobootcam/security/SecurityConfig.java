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
	            // Prioridad 1: Absolutamente público (incluyendo la página de construcción)
	            .requestMatchers("/css/**", "/js/**", "/img/**","/registro", "/construccion").permitAll()
	            .requestMatchers("/", "/home", "/login", "/registro", "/recetario").permitAll()
	            .requestMatchers("/catalogoLaChefa/**").permitAll()
	            
	            // Prioridad 2: Protegido
	            .requestMatchers("/nueva-receta", "/productos/**", "/ventas/**", "/admin/**").hasAuthority("ADMIN")
	            
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