package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectores")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping
    public List<SectorDTO> getAllSectores() {
        return sectorService.getAllSectores();
    }
}

