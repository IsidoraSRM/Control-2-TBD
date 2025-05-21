package com.tbd_g3.backend_c2.dto;

import com.tbd_g3.backend_c2.entity.SectorEntity;
import lombok.Data;

@Data
public class SectorDTO {
    private Integer idsector;
    private String nombre;
    private String descripcion;
    private Double lat;
    private Double lng;

    public SectorDTO(SectorEntity sector) {
        this.idsector = sector.getIdsector();
        this.nombre = sector.getNombre();
        this.descripcion = sector.getDescripcion();
        if (sector.getLocalizacion() != null) {
            this.lat = sector.getLocalizacion().getY();
            this.lng = sector.getLocalizacion().getX();
        }
    }
}
