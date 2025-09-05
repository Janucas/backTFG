package com.tfg.backend.persistance.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipajeDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "equipaje_id")
    private Equipaje equipaje;

    @OneToMany(mappedBy = "equipajeDia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEquipaje> items;

    @OneToOne(mappedBy = "equipajeDia", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClimaDia clima;
}
