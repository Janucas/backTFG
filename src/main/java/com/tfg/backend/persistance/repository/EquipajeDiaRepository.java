package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.EquipajeDia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipajeDiaRepository extends JpaRepository<EquipajeDia, Integer> {
    List<EquipajeDia> findByEquipajeId(Integer equipajeId);

}
