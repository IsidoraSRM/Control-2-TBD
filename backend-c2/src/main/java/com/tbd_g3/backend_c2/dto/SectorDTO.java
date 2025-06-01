package com.tbd_g3.backend_c2.dto;

import com.tbd_g3.backend_c2.entity.SectorEntity;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;    // Si necesitas puntos
import org.locationtech.jts.geom.Polygon;
@Data
public class SectorDTO {
    private Integer idsector;
    private String nombre;
    private String descripcion;
    private List<Coordenada> vertices;  // Para representar el polígono
    private Coordenada centroide;       // Para referencia central

    @Data
    public static class Coordenada {
        private Double lat;
        private Double lng;
    }

    public SectorDTO(SectorEntity sector) {
        this.idsector = sector.getIdsector();
        this.nombre = sector.getNombre();
        this.descripcion = sector.getDescripcion();

        if (sector.getLocalizacion() != null) {
            // Obtener los vértices del polígono en el orden [lat, lng] para Leaflet
            this.vertices = Arrays.stream(sector.getLocalizacion().getCoordinates())
                    .map(coord -> {
                        Coordenada c = new Coordenada();
                        c.setLat(coord.getY()); // Y es latitud
                        c.setLng(coord.getX()); // X es longitud
                        return c;
                    })
                    .collect(Collectors.toList());

            // Calcular el centroide
            Point centroid = sector.getLocalizacion().getCentroid();
            this.centroide = new Coordenada();
            this.centroide.setLat(centroid.getY());
            this.centroide.setLng(centroid.getX());
        }
    }
}