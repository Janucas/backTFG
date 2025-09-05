package com.tfg.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase utilitaria para generar y validar JSON Web Tokens (JWT).
 */
@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens. Debe tener al menos 32 bytes para HS256.
    // En producción, es recomendable cargarla desde application.properties o desde Vault.
    private static final String SECRET_KEY = "clave-secreta-super-segura-de-mas-de-32-caracteres";

    // Tiempo de vida del token en milisegundos (aquí, 100 años como ejemplo).
    // Ajusta según tus necesidades (p.ej. 24 horas = 1000*60*60*24).
    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24 * 365 * 100;

    /**
     * Genera un JWT con:
     *  - subject = username
     *  - issuedAt = ahora
     *  - expiration = ahora + EXPIRATION_MS
     *  - firmado con HS256 y SECRET_KEY
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae un único claim (por ejemplo, subject o expiration) del token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extrae el "subject" del token, que en este caso es el username (email).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Comprueba si el token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Valida que el token pertenezca al username dado y que no esté expirado.
     */
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    /**
     * Obtiene una Key para firmar/verificar JWTs usando HS256.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Extrae todos los Claims del JWT (lanza excepción si el token no es válido).
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
