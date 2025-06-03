package com.tfg.backend.security;

import com.tfg.backend.services.CustomUserDetailsService;   // ← IMPORT CORRECTO
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de Spring Security:
 *   • Desactivar CSRF (usamos JWT). 
 *   • Permitir CORS desde http://localhost:3000.
 *   • Dejar libres /auth/**.
 *   • Proteger /api/equipajes/** y /api/items/** (exigen JWT válido).
 *   • Cualquier otra ruta queda permitida (por ej. /actuator, /health, etc.).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Desactivar CSRF: no usamos cookies de sesión, sino JWT
            .csrf(csrf -> csrf.disable())

            // 2. Habilitar CORS (configuración en corsFilter())
            .cors(cors -> {})

            // 3. Configurar permisos por ruta
            .authorizeHttpRequests(auth -> auth
                // -- Endpoints de autenticación quedan libres:
                .requestMatchers("/auth/**").permitAll()

                // -- Proteger estas rutas: exigen JWT
                .requestMatchers("/api/equipajes/**").authenticated()
                .requestMatchers("/api/items/**").authenticated()

                // -- Cualquier otra petición queda libre
                .anyRequest().permitAll()
            )

            // 4. Stateless: no guardamos sesión HTTP en el servidor
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 5. Prepend our JWT filter before the default UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
    }

    /**
     * Bean para permitir CORS desde el front (http://localhost:3000).
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000")); 
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Exponer el AuthenticationManager para que AuthController pueda inyectarlo.
     */
   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}


    /**
     * Bean para encriptar / validar contraseñas con BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
