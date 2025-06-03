package com.tfg.backend.services;

import com.tfg.backend.dto.EquipajeRequest;
import com.tfg.backend.dto.InformeEquipajeResponse;
import com.tfg.backend.persistance.models.*;
import com.tfg.backend.persistance.repository.ClimaDiaRepository;
import com.tfg.backend.persistance.repository.EquipajeDiaRepository;
import com.tfg.backend.persistance.repository.EquipajeRepository;
import org.springframework.stereotype.Service;
import com.tfg.backend.dto.EquipajeHistorialDto;
import com.tfg.backend.persistance.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class EquipajeService {

    private final EquipajeRepository equipajeRepository;
    private final EquipajeDiaRepository equipajeDiaRepository;
    private final ClimaDiaRepository climaDiaRepository;
    private final WeatherService weatherService;
    private final ItemEquipajeService itemEquipajeService;
    private final UsuarioRepository usuarioRepository; // <-- NUEVO

    public EquipajeService(
        EquipajeRepository equipajeRepository,
        EquipajeDiaRepository equipajeDiaRepository,
        ClimaDiaRepository climaDiaRepository,
        WeatherService weatherService,
        ItemEquipajeService itemEquipajeService,
        UsuarioRepository usuarioRepository // <-- NUEVO
    ) {
        this.equipajeRepository = equipajeRepository;
        this.equipajeDiaRepository = equipajeDiaRepository;
        this.climaDiaRepository = climaDiaRepository;
        this.weatherService = weatherService;
        this.itemEquipajeService = itemEquipajeService;
        this.usuarioRepository = usuarioRepository;
    }

    public Equipaje crearEquipaje(EquipajeRequest request, Usuario usuario) {
        Equipaje equipaje = new Equipaje();
        equipaje.setDestino(request.getDestino());
        equipaje.setFechaSalida(request.getFechaInicio());
        equipaje.setFechaRegreso(request.getFechaFin());
        equipaje.setUsuario(usuario);
        equipaje = equipajeRepository.save(equipaje);

        List<EquipajeDia> dias = new ArrayList<>();
        Map<LocalDate, ClimaDia> climas = weatherService.obtenerClimaPorCiudadYFechas(
                request.getDestino(), request.getFechaInicio(), request.getFechaFin());

        LocalDate fecha = request.getFechaInicio();
        while (!fecha.isAfter(request.getFechaFin())) {
            EquipajeDia dia = new EquipajeDia();
            dia.setFecha(fecha);
            dia.setEquipaje(equipaje);
            dia = equipajeDiaRepository.save(dia);
            dias.add(dia);

            // Guardar clima si está disponible
            if (climas.containsKey(fecha)) {
                ClimaDia clima = climas.get(fecha);
                clima.setEquipajeDia(dia);
                clima = climaDiaRepository.save(clima);

                // Generar ítems de equipaje personalizados según el clima
                itemEquipajeService.generarItemsPorClima(dia, clima);
            }

            fecha = fecha.plusDays(1);
        }

        equipaje.setDias(dias);
        return equipaje;
    }

    public List<InformeEquipajeResponse> generarInformeEquipaje(Equipaje equipaje) {
        List<InformeEquipajeResponse> informe = new ArrayList<>();

        for (EquipajeDia dia : equipaje.getDias()) {
            StringBuilder sb = new StringBuilder();

            String diaSemana = dia.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).toLowerCase();
            String fechaTexto = dia.getFecha().format(DateTimeFormatter.ofPattern("d 'de' MMMM", Locale.forLanguageTag("es")));

            sb.append("🗓️ ").append(capitalize(diaSemana)).append(", ").append(fechaTexto).append(":\n\n");

            // Clima
            if (dia.getClima() != null) {
                sb.append("Clima: ").append(capitalize(dia.getClima().getDescripcion())).append("\n\n");
                sb.append("Temperaturas: Máxima de ")
                        .append(Math.round(dia.getClima().getTemperaturaMax())).append("°C, mínima de ")
                        .append(Math.round(dia.getClima().getTemperaturaMin())).append("°C\n\n");
            }

            // Ítems
            sb.append("Recomendaciones de equipaje:\n");
            for (ItemEquipaje item : dia.getItems()) {
                sb.append("- ").append(capitalize(item.getNombre())).append("\n");
            }

            informe.add(new InformeEquipajeResponse(sb.toString()));
        }

        return informe;
    }

    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

     public List<EquipajeHistorialDto> obtenerHistorialPorUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) return Collections.emptyList();

        Usuario usuario = usuarioOpt.get();
        List<Equipaje> equipajes = equipajeRepository.findByUsuarioOrderByCreadoEnDesc(usuario);



        return equipajes.stream().map(e -> new EquipajeHistorialDto(
        e.getId().longValue(), // 👈 conversión explícita
        e.getDestino(),
        e.getFechaSalida(),
        e.getFechaRegreso()
    )).toList();

    }

    public boolean eliminarEquipajePorUsuario(Integer equipajeId, String email){
    Optional<Equipaje> equipajeOpt = equipajeRepository.findById(equipajeId);
    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

    if (equipajeOpt.isEmpty() || usuarioOpt.isEmpty()) return false;

    Equipaje equipaje = equipajeOpt.get();
    Usuario usuario = usuarioOpt.get();

    if (!equipaje.getUsuario().getId().equals(usuario.getId())) {
        return false; // el equipaje no pertenece al usuario autenticado
    }

    equipajeRepository.delete(equipaje);
    return true;
}

}
