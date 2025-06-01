package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.repository.SectorRepository;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorService {
    @Autowired
    private SectorRepository sectorRepository;

    public List<SectorDTO> getAllSectores() {
        return sectorRepository.findAll().stream()
                .map(SectorDTO::new)
                .collect(Collectors.toList());
    }

    public SectorDTO getSectorWithMostCompletedTasks(Point userLocation) {
        SectorEntity sector = sectorRepository.findSectorWithMostCompletedTasks(userLocation);
        return new SectorDTO(sector);
    }

    public SectorDTO buscarSectorConMasTareasCompletadasEn5km(Point userLocation) {
        SectorEntity sector = sectorRepository.buscarSectorConMasTareasCompletadasEn5km(userLocation);
        return new SectorDTO(sector); // Convertir la entidad a DTO antes de devolverla
    }




}
