package com.tfg.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username; // aquí llegará el email
    private String password;
    private String nombre;   // nuevo campo
}
