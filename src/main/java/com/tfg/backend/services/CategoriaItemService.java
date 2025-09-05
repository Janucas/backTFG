package com.tfg.backend.services;

import com.tfg.backend.persistance.models.Categoria;
import com.tfg.backend.persistance.repository.CategoriaItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaItemService {

    private final CategoriaItemRepository categoriaItemRepository;

    public CategoriaItemService(CategoriaItemRepository categoriaItemRepository) {
        this.categoriaItemRepository = categoriaItemRepository;
    }

    public List<Categoria> findAll() {
        return categoriaItemRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return categoriaItemRepository.save(categoria);
    }

    public void deleteById(Integer id) {
        categoriaItemRepository.deleteById(id);
    }
}
