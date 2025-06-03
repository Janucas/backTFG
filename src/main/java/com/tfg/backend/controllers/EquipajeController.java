package com.tfg.backend.controllers;

import com.tfg.backend.dto.EquipajeCompletoResponse;
import com.tfg.backend.dto.EquipajeHistorialDto;
import com.tfg.backend.dto.EquipajeRequest;
import com.tfg.backend.dto.InformeEquipajeResponse;
import com.tfg.backend.persistance.models.Equipaje;
import com.tfg.backend.persistance.models.Usuario;
import com.tfg.backend.persistance.repository.EquipajeRepository;
import com.tfg.backend.persistance.repository.UsuarioRepository;
import com.tfg.backend.services.EquipajeService;
import com.tfg.backend.utils.PdfUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/equipajes")
public class EquipajeController {

    private final EquipajeRepository equipajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipajeService equipajeService;

    public EquipajeController(
            EquipajeRepository equipajeRepository,
            UsuarioRepository usuarioRepository,
            EquipajeService equipajeService
    ) {
        this.equipajeRepository = equipajeRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipajeService = equipajeService;
    }

    // Crear un equipaje con días e ítems automáticos
    @PostMapping
public ResponseEntity<?> crearEquipaje(@RequestBody EquipajeRequest request) {
    // Obtener usuario autenticado
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
    if (usuario == null) {
        return ResponseEntity.status(401).body("Usuario no autenticado");
    }

    // Lógica delegada al servicio
    Equipaje equipaje = equipajeService.crearEquipaje(request, usuario);

    return ResponseEntity.ok("Equipaje y días generados correctamente");
}


    // Ver el contenido completo del equipaje (días e ítems)
  @GetMapping("/{id}")
public ResponseEntity<EquipajeCompletoResponse> verEquipajeCompleto(@PathVariable Integer id) {
    Equipaje equipaje = equipajeRepository.findById(id).orElse(null);
    if (equipaje == null) {
        return ResponseEntity.notFound().build();
    }

    EquipajeCompletoResponse response = new EquipajeCompletoResponse();
    response.setId(equipaje.getId());
    response.setDestino(equipaje.getDestino());
    response.setFechaSalida(equipaje.getFechaSalida());
    response.setFechaRegreso(equipaje.getFechaRegreso());

    List<EquipajeCompletoResponse.DiaEquipajeResponse> dias = equipaje.getDias().stream().map(dia -> {
        EquipajeCompletoResponse.DiaEquipajeResponse diaDTO = new EquipajeCompletoResponse.DiaEquipajeResponse();
        diaDTO.setFecha(dia.getFecha());

        // Clima
        if (dia.getClima() != null) {
            EquipajeCompletoResponse.ClimaResponse climaDTO = new EquipajeCompletoResponse.ClimaResponse();
            climaDTO.setTemperaturaMin(dia.getClima().getTemperaturaMin());
            climaDTO.setTemperaturaMax(dia.getClima().getTemperaturaMax());
            climaDTO.setDescripcion(dia.getClima().getDescripcion());
            diaDTO.setClima(climaDTO);
        }

        // Ítems
        List<EquipajeCompletoResponse.ItemResponse> items = dia.getItems().stream().map(item -> {
            EquipajeCompletoResponse.ItemResponse itemDTO = new EquipajeCompletoResponse.ItemResponse();
            itemDTO.setNombre(item.getNombre());
            return itemDTO;
        }).toList();

        diaDTO.setItems(items);
        return diaDTO;
    }).toList();

    response.setDias(dias);
    return ResponseEntity.ok(response);
}

@GetMapping("/{id}/informe")
public ResponseEntity<List<InformeEquipajeResponse>> obtenerInforme(@PathVariable Integer id) {
    Optional<Equipaje> equipajeOpt = equipajeRepository.findById(id);
    if (equipajeOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    List<InformeEquipajeResponse> informe = equipajeService.generarInformeEquipaje(equipajeOpt.get());
    return ResponseEntity.ok(informe);
}

@GetMapping("/historial")
public ResponseEntity<List<EquipajeHistorialDto>> obtenerHistorialUsuario() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    List<EquipajeHistorialDto> historial = equipajeService.obtenerHistorialPorUsuario(email);
    return ResponseEntity.ok(historial);
}

@DeleteMapping("/{id}")
public ResponseEntity<?> eliminarEquipaje(@PathVariable Integer id)
 {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    boolean eliminado = equipajeService.eliminarEquipajePorUsuario(id, email);

    if (eliminado) {
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.status(403).body("No tienes permiso para eliminar este equipaje.");
    }
}

@GetMapping("/{id}/informe-pdf")
public ResponseEntity<byte[]> descargarInformePdf(@PathVariable Integer id)
 {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    Optional<Equipaje> equipajeOpt = equipajeRepository.findById(id);
    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

    if (equipajeOpt.isEmpty() || usuarioOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Equipaje equipaje = equipajeOpt.get();
    Usuario usuario = usuarioOpt.get();

    if (!equipaje.getUsuario().getId().equals(usuario.getId())) {
        return ResponseEntity.status(403).build();
    }

    List<InformeEquipajeResponse> informe = equipajeService.generarInformeEquipaje(equipaje);
    byte[] pdfBytes = PdfUtil.generarPdfDesdeInforme(informe); // <-- este método lo implementamos ahora

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=equipaje_" + id + ".pdf")
            .header("Content-Type", "application/pdf")
            .body(pdfBytes);
}






}
