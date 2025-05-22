package com.tbd_g3.backend_c2.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String nombreUsuario;
    private String contrasena;
    private String correo;
    private LocalDateTime fechaRegistro;
    private String rol;
    private Double latitud;  // Se envía latitud en lugar de `Point`
    private Double longitud; // Se envía longitud en lugar de `Point`
}

