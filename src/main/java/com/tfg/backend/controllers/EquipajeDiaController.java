package com.tfg.backend.controllers;

import com.tfg.backend.persistance.models.Equipaje;
import com.tfg.backend.persistance.models.EquipajeDia;
import com.tfg.backend.persistance.repository.EquipajeDiaRepository;
import com.tfg.backend.persistance.repository.EquipajeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipaje-dias")
public class EquipajeDiaController {

    private final EquipajeDiaRepository equipajeDiaRepository;
    private final EquipajeRepository equipajeRepository;

    public EquipajeDiaController(EquipajeDiaRepository equipajeDiaRepository, EquipajeRepository equipajeRepository) {
        this.equipajeDiaRepository = equipajeDiaRepository;
        this.equipajeRepository = equipajeRepository;
    }

    // GET /api/equipaje-dias/{equipajeId}
    @GetMapping("/{equipajeId}")
    public ResponseEntity<?> obtenerDiasPorEquipaje(@PathVariable Integer equipajeId) {
        Equipaje equipaje = equipajeRepository.findById(equipajeId).orElse(null);
        if (equipaje == null) {
            return ResponseEntity.notFound().build();
        }

        List<EquipajeDia> dias = equipajeDiaRepository.findByEquipajeId(equipajeId);
        return ResponseEntity.ok(dias);
    }
}
