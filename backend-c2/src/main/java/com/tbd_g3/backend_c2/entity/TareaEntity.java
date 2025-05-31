package com.tbd_g3.backend_c2.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Entity
@Table(name = "tareas")
@Data
public class TareaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idtarea;

    private String titulo;

    private String descripcion;

    private LocalDate fechavencimiento;

    private String estado;
    @Column(name = "localizacion", columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point localizacion;

    private Integer idusuario;

    private Integer idsector;
}

