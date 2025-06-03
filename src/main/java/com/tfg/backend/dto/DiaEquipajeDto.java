package com.tfg.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DiaEquipajeDto {
    private LocalDate fecha;
    private String clima;
    private Double temperaturaMin;
    private Double temperaturaMax;
    private List<ItemEquipajeDto> items;
}
