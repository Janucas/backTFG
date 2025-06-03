package com.tfg.backend.persistance.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clima_dia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClimaDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private double temperaturaMin;

    private double temperaturaMax;

    private String descripcion;

    @OneToOne
    @JoinColumn(name = "equipaje_dia_id")
    private EquipajeDia equipajeDia;
}

