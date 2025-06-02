package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.entity.UsuarioEntity;
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

    @Autowired
    private UsuarioService userService;

    public List<SectorDTO> getAllSectores() {
        return sectorRepository.findAll().stream()
                .map(SectorDTO::new)
                .collect(Collectors.toList());
    }

    public SectorDTO getSectorWithMostCompletedTasks(Point userLocation) {
        SectorEntity sector = sectorRepository.findSectorWithMostCompletedTasks(userLocation);
        return new SectorDTO(sector);
    }

    // Nuevo método: obtiene el sector top a partir del id del usuario
    public SectorDTO getSectorForUser(Integer userId) {
        // Se obtiene el usuario usando el userId
        UsuarioEntity user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado para id: " + userId);
        }

        // Se espera que el usuario tenga la ubicación (tipo Point) almacenada
        Point userLocation = user.getLocalizacion();
        if (userLocation == null) {
            throw new RuntimeException("El usuario no tiene asignada una ubicación.");
        }

        // Configuramos el SRID para que las operaciones geoespaciales funcionen correctamente
        userLocation.setSRID(4326);

        // Usamos el método existente para obtener el sector en función de la ubicación
        return getSectorWithMostCompletedTasks(userLocation);
    }


    public SectorDTO getSectorForUser5km(Integer userId) {
        UsuarioEntity user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado para id: " + userId);
        }
        Point userLocation = user.getLocalizacion();
        if (userLocation == null) {
            throw new RuntimeException("El usuario no tiene asignada una ubicación.");
        }
        userLocation.setSRID(4326);
        return buscarSectorConMasTareasCompletadasEn5km(userLocation);
    }

    public SectorDTO buscarSectorConMasTareasCompletadasEn5km(Point userLocation) {
        SectorEntity sector = sectorRepository.buscarSectorConMasTareasCompletadasEn5km(userLocation);
        return new SectorDTO(sector);
    }
}






