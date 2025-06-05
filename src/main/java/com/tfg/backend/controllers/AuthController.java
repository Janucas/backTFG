package com.tfg.backend.controllers;

import com.tfg.backend.dto.AuthRequest;
import com.tfg.backend.dto.AuthResponse;
import com.tfg.backend.persistance.models.Usuario;
import com.tfg.backend.persistance.repository.UsuarioRepository;
import com.tfg.backend.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// 👇👇 Añade esta línea justo antes del controlador
//@CrossOrigin(origins = "https://fronttfg.onrender.com")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(
        AuthenticationManager authenticationManager,
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        if (usuarioRepository.existsByEmail(request.getUsername())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getUsername());
        usuario.setRole("USER");

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = usuarioRepository.findByUsername(request.getUsername())
            .map(usuario -> org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRole())
                .build())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login-google")
public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody AuthRequest request) {
    String email = request.getUsername(); // en este caso, el email viene en username
    String nombre = request.getPassword(); // usaremos el campo password para el nombre (truco rápido)

    Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
        Usuario nuevo = new Usuario();
        nuevo.setEmail(email);
        nuevo.setUsername(email);
        nuevo.setPassword(passwordEncoder.encode("google-auth")); // no se usará
        nuevo.setRole("USER");
        return usuarioRepository.save(nuevo);
    });

    // Generar JWT
    String token = jwtUtil.generateToken(usuario.getUsername());
    return ResponseEntity.ok(new AuthResponse(token));
}

}
