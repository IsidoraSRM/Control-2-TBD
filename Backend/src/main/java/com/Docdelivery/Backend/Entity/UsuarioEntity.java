package com.Docdelivery.Backend.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {
    private long idUsuario;
    private String rut;
    private String nameParam;
    private String email;
    private String phone;
    @JsonFormat
    private Date birthdate;
    private String password;
    private String role;

}