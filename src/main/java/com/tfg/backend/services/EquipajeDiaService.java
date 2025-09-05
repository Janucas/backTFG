package com.tfg.backend.services;

import com.tfg.backend.persistance.models.EquipajeDia;
import com.tfg.backend.persistance.repository.EquipajeDiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipajeDiaService {

    private final EquipajeDiaRepository equipajeDiaRepository;

    public EquipajeDiaService(EquipajeDiaRepository equipajeDiaRepository) {
        this.equipajeDiaRepository = equipajeDiaRepository;
    }

    public List<EquipajeDia> guardarTodos(List<EquipajeDia> dias) {
        return equipajeDiaRepository.saveAll(dias);
    }
}
