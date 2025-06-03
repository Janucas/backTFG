package com.tfg.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EquipajeHistorialDto {
    private Long id;
    private String destino;
    private LocalDate fechaSalida;
    private LocalDate fechaRegreso;
    private List<DiaEquipajeDto> dias;

    // Constructor para vista de historial (sin los días)
    public EquipajeHistorialDto(Long id, String destino, LocalDate fechaSalida, LocalDate fechaRegreso) {
        this.id = id;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaRegreso = fechaRegreso;
    }
}
