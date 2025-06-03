package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
     Optional<Usuario> findByEmail(String email);
     
}
