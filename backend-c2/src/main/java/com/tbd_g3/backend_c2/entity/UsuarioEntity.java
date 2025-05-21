package com.tbd_g3.backend_c2.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "nombreusuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "contraseña", nullable = false)
    private String contraseña;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "localizacion", columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point localizacion;

    @Column(name = "fecharegistro")
    private LocalDateTime fechaRegistro;
}

