package com.tfg.backend.services;

import com.tfg.backend.persistance.models.ClimaDia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openweather.api.key}")
    private String apiKey;

    public Map<LocalDate, ClimaDia> obtenerClimaPorCiudadYFechas(String ciudad, LocalDate fechaInicio, LocalDate fechaFin) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric&lang=es",
                ciudad, apiKey
        );

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> lista = (List<Map<String, Object>>) response.get("list");

        Map<LocalDate, List<Double>> tempMinPorDia = new HashMap<>();
        Map<LocalDate, List<Double>> tempMaxPorDia = new HashMap<>();
        Map<LocalDate, String> descripcionPorDia = new HashMap<>();

        for (Map<String, Object> item : lista) {
            String fechaHora = (String) item.get("dt_txt");
            LocalDate fecha = LocalDate.parse(fechaHora.substring(0, 10));

            if (fecha.isBefore(fechaInicio) || fecha.isAfter(fechaFin)) continue;

            Map<String, Object> main = (Map<String, Object>) item.get("main");
            double tempMin = ((Number) main.get("temp_min")).doubleValue();
            double tempMax = ((Number) main.get("temp_max")).doubleValue();

            List<Map<String, Object>> weather = (List<Map<String, Object>>) item.get("weather");
            String descripcion = (String) weather.get(0).get("description");

            tempMinPorDia.computeIfAbsent(fecha, k -> new ArrayList<>()).add(tempMin);
            tempMaxPorDia.computeIfAbsent(fecha, k -> new ArrayList<>()).add(tempMax);
            descripcionPorDia.putIfAbsent(fecha, descripcion);
        }

        Map<LocalDate, ClimaDia> resultado = new HashMap<>();
        for (LocalDate fecha : tempMinPorDia.keySet()) {
            double min = tempMinPorDia.get(fecha).stream().min(Double::compare).orElse(0.0);
            double max = tempMaxPorDia.get(fecha).stream().max(Double::compare).orElse(0.0);
            String descripcion = descripcionPorDia.getOrDefault(fecha, "sin datos");

            resultado.put(fecha, ClimaDia.builder()
                    .fecha(fecha)
                    .temperaturaMin(min)
                    .temperaturaMax(max)
                    .descripcion(descripcion)
                    .build());
        }

        return resultado;
    }
}
