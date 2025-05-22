package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import com.tbd_g3.backend_c2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioEntity> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

}