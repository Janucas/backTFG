package com.tfg.backend.persistance.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_item") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
}
