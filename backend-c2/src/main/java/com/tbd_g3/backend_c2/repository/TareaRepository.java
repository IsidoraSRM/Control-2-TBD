package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<TareaEntity, Integer> {
}

