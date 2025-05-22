package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<TareaEntity, Integer> {



    @Query(value = "SELECT * FROM filtrar_tareas(:estado, :palabraClave)", nativeQuery = true)
    List<Object[]> filtrarTareas(
            @Param("estado") String estado,
            @Param("palabraClave") String palabraClave
    );

    @Query(value = """
        SELECT 
            t.idtarea,
            t.titulo,
            s.nombre AS nombre_sector,
            t.localizacion,
            ST_Distance(u.localizacion::geography, t.localizacion::geography) AS distancia_metros,
            t.fechavencimiento
        FROM 
            tareas t
        JOIN 
            usuarios u ON u.idusuario = :idUsuario
        JOIN 
            sectores s ON t.idsector = s.idsector
        WHERE 
            t.estado = 'PENDIENTE'
        ORDER BY 
            distancia_metros ASC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findTareaMasCercana(@Param("idUsuario") Integer idUsuario);


}

