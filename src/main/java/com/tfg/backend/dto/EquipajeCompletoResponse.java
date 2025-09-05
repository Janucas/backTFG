package com.tfg.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EquipajeCompletoResponse {

    private Integer id;
    private String destino;
    private LocalDate fechaSalida;
    private LocalDate fechaRegreso;
    private List<DiaEquipajeResponse> dias;

    @Data
    public static class DiaEquipajeResponse {
        private LocalDate fecha;
        private ClimaResponse clima;
        private List<ItemResponse> items;
    }

    @Data
    public static class ClimaResponse {
        private double temperaturaMin;
        private double temperaturaMax;
        private String descripcion;
    }

    @Data
    public static class ItemResponse {
        private String nombre;
    }
}
