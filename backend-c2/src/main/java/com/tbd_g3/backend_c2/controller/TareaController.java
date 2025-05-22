package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.TareaDTO;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<TareaDTO> getAllTareas() {
        return tareaService.getAllTareas();
    }
}

