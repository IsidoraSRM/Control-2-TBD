package com.tbd_g3.backend_c2.dto;

import com.tbd_g3.backend_c2.entity.TareaEntity;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Data
public class TareaDTO {
    private Integer idtarea;
    private String titulo;
    private String descripcion;
    private LocalDate fechavencimiento;
    private String estado;
    private Double lat;
    private Double lng;
    private Integer idusuario;
    private Integer idsector;

    public TareaDTO(TareaEntity tarea) {
        this.idtarea = tarea.getIdtarea();
        this.titulo = tarea.getTitulo();
        this.descripcion = tarea.getDescripcion();
        this.fechavencimiento = tarea.getFechavencimiento();
        this.estado = tarea.getEstado();

        Point localizacion = tarea.getLocalizacion();
        if (localizacion != null) {
            this.lat = localizacion.getY(); // latitud
            this.lng = localizacion.getX(); // longitud
        }

        this.idusuario = tarea.getIdusuario();
        this.idsector = tarea.getIdsector();
    }
}

