package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.ItemEquipaje;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEquipajeRepository extends JpaRepository<ItemEquipaje, Integer> {
    List<ItemEquipaje> findByEquipajeDiaId(Integer equipajeDiaId);

}
