package com.tfg.backend.services;

import com.tfg.backend.persistance.models.Categoria;
import com.tfg.backend.persistance.models.ClimaDia;
import com.tfg.backend.persistance.models.EquipajeDia;
import com.tfg.backend.persistance.models.ItemEquipaje;
import com.tfg.backend.persistance.repository.CategoriaRepository;
import com.tfg.backend.persistance.repository.ItemEquipajeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemEquipajeService {

    private final ItemEquipajeRepository itemEquipajeRepository;
    private final CategoriaRepository categoriaRepository;

    public ItemEquipajeService(ItemEquipajeRepository itemEquipajeRepository, CategoriaRepository categoriaRepository) {
        this.itemEquipajeRepository = itemEquipajeRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public void generarItemsPorClima(EquipajeDia dia, ClimaDia clima) {
        List<ItemEquipaje> items = new ArrayList<>();

        double tempMax = clima.getTemperaturaMax();
        double tempMin = clima.getTemperaturaMin();
        String descripcion = clima.getDescripcion().toLowerCase();

        // Ropa ligera
        if (tempMax >= 28) {
            items.add(crearItem("Ropa ligera", "Ropa", dia));
            items.add(crearItem("Gafas de sol", "Accesorios", dia));
            items.add(crearItem("Protector solar", "Protección solar", dia));
        }

        // Clima templado
        if (tempMax >= 20 && tempMax < 28) {
            items.add(crearItem("Ropa de manga corta", "Ropa", dia));
            items.add(crearItem("Chaqueta ligera", "Ropa", dia));
        }

        // Clima fresco
        if (tempMax < 20 || tempMin < 12) {
            items.add(crearItem("Ropa de manga larga", "Ropa", dia));
            items.add(crearItem("Jersey o sudadera", "Ropa", dia));
        }

        // Frío
        if (tempMin < 6) {
            items.add(crearItem("Abrigo", "Ropa", dia));
            items.add(crearItem("Bufanda", "Accesorios", dia));
        }

        // Lluvia
        if (descripcion.contains("lluvia") || descripcion.contains("chubasco") || descripcion.contains("tormenta")) {
            items.add(crearItem("Paraguas", "Accesorios", dia));
            items.add(crearItem("Chubasquero", "Ropa", dia));
            items.add(crearItem("Zapatos impermeables", "Calzado", dia));
        }

        // Sol fuerte
        if (descripcion.contains("despejado") || descripcion.contains("soleado") || descripcion.contains("sol")) {
            items.add(crearItem("Sombrero o gorra", "Accesorios", dia));
        }

        itemEquipajeRepository.saveAll(items);
    }

    private ItemEquipaje crearItem(String nombre, String nombreCategoria, EquipajeDia dia) {
        Categoria categoria = categoriaRepository.findByNombreIgnoreCase(nombreCategoria)
                .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombreCategoria)));

        ItemEquipaje item = new ItemEquipaje();
        item.setNombre(nombre);
        item.setCategoria(categoria);
        item.setEquipajeDia(dia);
        return item;
    }
}
