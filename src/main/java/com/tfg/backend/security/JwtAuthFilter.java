package com.tfg.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tfg.backend.services.CustomUserDetailsService;

import java.io.IOException;

/**
 * Filtro que valida el JWT en cada petición. 
 * - Si la cabecera Authorization contiene "Bearer <jwt>", lo parsea. 
 * - Extrae el username del token y, si es válido, inyecta la autenticación en el contexto.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer la cabecera "Authorization"
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // 2. Si el header comienza con "Bearer ", extraer el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token inválido o mal formado: no autenticamos, pero no rompemos el flujo.
            }
        }

        // 3. Si obtuvimos un username y aún no hay autenticación en SpringContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar detalles de usuario desde BD
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4. Si el token es válido para este username, inyectar la autenticación
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
