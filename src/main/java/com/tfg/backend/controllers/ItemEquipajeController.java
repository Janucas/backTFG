package com.tfg.backend.controllers;

import com.tfg.backend.dto.ItemEquipajeResponse;
import com.tfg.backend.persistance.models.EquipajeDia;
import com.tfg.backend.persistance.repository.EquipajeDiaRepository;
import com.tfg.backend.persistance.repository.ItemEquipajeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemEquipajeController {

    private final ItemEquipajeRepository itemEquipajeRepository;
    private final EquipajeDiaRepository equipajeDiaRepository;

    public ItemEquipajeController(ItemEquipajeRepository itemEquipajeRepository,
                                  EquipajeDiaRepository equipajeDiaRepository) {
        this.itemEquipajeRepository = itemEquipajeRepository;
        this.equipajeDiaRepository = equipajeDiaRepository;
    }

    // GET /api/items/dia/{diaId}
    @GetMapping("/dia/{diaId}")
    public ResponseEntity<?> obtenerItemsPorDia(@PathVariable Integer diaId) {
        EquipajeDia dia = equipajeDiaRepository.findById(diaId).orElse(null);
        if (dia == null) {
            return ResponseEntity.notFound().build();
        }

        List<ItemEquipajeResponse> items = dia.getItems().stream().map(item ->
                new ItemEquipajeResponse(
                        item.getNombre(),
                        item.getCategoria() != null ? item.getCategoria().getNombre() : "Sin categoría"
                )
        ).toList();

        return ResponseEntity.ok(items);
    }
}
