package com.tfg.backend.persistance.repository;

import com.tfg.backend.persistance.models.Equipaje;
import com.tfg.backend.persistance.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipajeRepository extends JpaRepository<Equipaje, Integer> {
    List<Equipaje> findByUsuarioId(Integer usuarioId);
    List<Equipaje> findAllByUsuario(Usuario usuario);
    List<Equipaje> findByUsuario(Usuario usuario);
    List<Equipaje> findByUsuarioId(Long usuarioId);
    List<Equipaje> findByUsuarioOrderByCreadoEnDesc(Usuario usuario);



}
