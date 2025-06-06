package com.tbd_g3.backend_c2.dto;

import com.tbd_g3.backend_c2.entity.TareaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaDTO {
    private Integer idtarea;
    private String titulo;
    private String descripcion;
    private LocalDate fechavencimiento;
    private String estado;
    private double lat;
    private double lng;
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

