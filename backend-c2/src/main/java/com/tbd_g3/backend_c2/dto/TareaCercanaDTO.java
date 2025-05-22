package com.tbd_g3.backend_c2.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tbd_g3.backend_c2.config.PointSerializer;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Point;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaCercanaDTO {
    private Integer idTarea;
    private String titulo;
    private String nombreSector;

    @JsonSerialize(using = PointSerializer.class)
    private Point localizacion;

    private Double distanciaMetros;
    private LocalDate fechaVencimiento;
}