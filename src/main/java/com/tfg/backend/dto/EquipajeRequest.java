package com.tfg.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EquipajeRequest {
    private String destino;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
