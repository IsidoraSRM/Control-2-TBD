package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
}

