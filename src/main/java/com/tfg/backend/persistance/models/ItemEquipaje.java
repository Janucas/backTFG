package com.tfg.backend.persistance.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_equipaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEquipaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipaje_dia_id")
    private EquipajeDia equipajeDia;
}
