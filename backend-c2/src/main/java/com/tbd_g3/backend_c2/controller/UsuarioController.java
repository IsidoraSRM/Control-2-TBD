package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.UsuarioDTO;
import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import com.tbd_g3.backend_c2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        List<UsuarioEntity> usuarios = usuarioService.getAllUsuarios();
        return usuarios.stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }
}
