package com.tbd_g3.backend_c2.config;

import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.repository.SectorRepository;
import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectorDataLoader {

    @Autowired
    private SectorRepository sectorRepository;

    @PostConstruct
    public void init() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        // Polígono rectangular grande que cubre la mayor parte de la Región Metropolitana de Santiago
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(-70.958, -33.083), // Norponiente
            new Coordinate(-70.958, -33.890), // Surponiente
            new Coordinate(-70.365, -33.890), // Suroriente
            new Coordinate(-70.365, -33.083), // Nororiente
            new Coordinate(-70.958, -33.083)  // Cierra el polígono
        };
        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        Polygon polygon = geometryFactory.createPolygon(shell, null);

        SectorEntity sector = new SectorEntity();
        sector.setNombre("Región Metropolitana (Aproximado)");
        sector.setDescripcion("Sector de ejemplo que cubre la mayor parte de la Región Metropolitana de Santiago");
        sector.setLocalizacion(polygon);

        sectorRepository.save(sector);

    }
} 