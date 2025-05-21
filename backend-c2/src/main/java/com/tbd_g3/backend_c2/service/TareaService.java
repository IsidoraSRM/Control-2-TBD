package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.TareaDTO;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public List<TareaDTO> getAllTareas() {
        List<TareaEntity> tareas = tareaRepository.findAll();
        return tareas.stream().map(TareaDTO::new).collect(Collectors.toList());
    }
}
