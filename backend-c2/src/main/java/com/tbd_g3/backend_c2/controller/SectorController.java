package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.service.SectorService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectores")
@CrossOrigin("*")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping
    public List<SectorDTO> getAllSectores() {
        return sectorService.getAllSectores();
    }

    //Consulta 3
    @GetMapping("/top-sector")
    public ResponseEntity<SectorDTO> getTopSector(@RequestParam double lon, @RequestParam double lat) {
        // Crear el punto geográfico con la ubicación del usuario
        Point userLocation = new GeometryFactory().createPoint(new Coordinate(lon, lat));
        userLocation.setSRID(4326); // Definir el sistema de referencia espacial (WGS 84)

        // Obtener el sector con más tareas completadas en un radio de 2km
        SectorDTO sector = sectorService.getSectorWithMostCompletedTasks(userLocation);

        return ResponseEntity.ok(sector);
    }
    //consulta 8
    @GetMapping("/sector-top-5km")
    public ResponseEntity<SectorDTO> obtenerSectorTop5km(@RequestParam double lon, @RequestParam double lat) {
        Point userLocation = new GeometryFactory().createPoint(new Coordinate(lon, lat));
        userLocation.setSRID(4326);

        SectorDTO sector = sectorService.buscarSectorConMasTareasCompletadasEn5km(userLocation);
        return ResponseEntity.ok(sector);
    }

}

