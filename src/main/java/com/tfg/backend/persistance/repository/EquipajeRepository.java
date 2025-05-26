package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipajeRepository extends JpaRepository<Equipaje, Integer> {
    List<Equipaje> findByUsuarioId(Integer usuarioId);
}
