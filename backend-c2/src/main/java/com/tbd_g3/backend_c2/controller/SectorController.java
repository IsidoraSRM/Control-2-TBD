package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.service.SectorService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/top-sector-2km")
    public ResponseEntity<SectorDTO> getTopSectorByUser(@RequestParam Integer userId) {
        try {
            SectorDTO sector = sectorService.getSectorForUser(userId);
            return ResponseEntity.ok(sector);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //consulta 8
    @GetMapping("/top-sector-5km")
    public ResponseEntity<SectorDTO> obtenerSectorTop5kmByUser(@RequestParam Integer userId) {
        try {
            SectorDTO sector = sectorService.getSectorForUser5km(userId);
            return ResponseEntity.ok(sector);
        } catch (RuntimeException e) {
            // Aquí podrías devolver un mensaje de error más específico o usar otro código de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}

