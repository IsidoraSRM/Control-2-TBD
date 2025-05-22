package com.tbd_g3.backend_c2.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String correo;
    private String contrasena;
}
