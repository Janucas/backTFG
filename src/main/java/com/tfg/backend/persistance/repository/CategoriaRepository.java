package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    Optional<Categoria> findByNombre(String nombre);

}
