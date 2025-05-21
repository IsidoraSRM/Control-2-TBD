package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.SectorDTO;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.repository.SectorRepository;
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
}
