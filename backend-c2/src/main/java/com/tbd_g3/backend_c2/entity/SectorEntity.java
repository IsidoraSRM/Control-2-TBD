package com.tbd_g3.backend_c2.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "sectores")
@Data
public class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idsector;

    private String nombre;
    private String descripcion;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point localizacion;
}
