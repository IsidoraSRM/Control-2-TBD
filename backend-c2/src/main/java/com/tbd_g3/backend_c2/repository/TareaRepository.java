package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    // ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes?
    @Query(value = """
        SELECT 
            s.nombre AS nombreSector,
            COUNT(t.id_tarea) AS cantidadTareasPendientes,
            s.localizacion
        FROM 
            tareas t
        JOIN 
            sectores s ON t.id_sector = s.id_sector
        WHERE 
            t.estado = 'PENDIENTE'
        GROUP BY 
            s.id_sector, s.nombre, s.localizacion
        HAVING 
            COUNT(t.id_tarea) = (
                SELECT MAX(cantidad)
                FROM (
                    SELECT COUNT(*) AS cantidad
                    FROM tareas
                    WHERE estado = 'PENDIENTE'
                    GROUP BY id_sector
                ) AS subquery
            )
        """, nativeQuery = true)
    List<Object[]> findSectoresConMasTareasPendientes();
    // ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?
    @Query(value = """
        SELECT 
            u.nombre_usuario,
            t.id_tarea,
            t.titulo,
            s.nombre AS nombre_sector,
            t.localizacion AS localizacion_tarea,
            u.localizacion AS localizacion_usuario,
            ST_Distance(u.localizacion::geography, t.localizacion::geography) AS distancia_metros,
            t.fecha_vencimiento
        FROM 
            tareas t
        JOIN 
            usuarios u ON u.id_usuario = :idUsuario
        JOIN 
            sectores s ON t.id_sector = s.id_sector
        WHERE 
            t.estado = 'PENDIENTE'
        ORDER BY 
            ST_Distance(u.localizacion::geography, t.localizacion::geography)
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findTareaPendienteMasCercana(@Param("idUsuario") Integer idUsuario);

    // ¿Cuál es el promedio de distancia entre las tareas completadas y el punto registrado del usuario?
    @Query(value = """
        SELECT 
            u.id_usuario,
            u.nombre_usuario,
            AVG(ST_Distance(u.localizacion::geography, t.localizacion::geography)) AS promedio_distancia_metros
        FROM 
            tareas t
        JOIN 
            usuarios u ON u.id_usuario = :idUsuario
        WHERE 
            t.estado = 'COMPLETADA'
            AND t.id_usuario = u.id_usuario
        GROUP BY 
            u.id_usuario, u.nombre_usuario
        """, nativeQuery = true)
    List<Object[]> findPromedioDistanciaTareasCompletadas(@Param("idUsuario") Integer idUsuario);


    // ¿Cuál es el promedio global de distancia de las tareas completadas respecto a la ubicación de los usuarios?
    @Query(value = """
    SELECT 
        ROUND(AVG(ST_Distance(u.localizacion::geography, t.localizacion::geography))::numeric, 2) AS promedio_distancia_metros
    FROM 
        tareas t
    JOIN 
        usuarios u ON t.id_usuario = u.id_usuario
    WHERE 
        LOWER(t.estado) = 'completada'
    """, nativeQuery = true)
    Double findPromedioGlobalDistanciaTareasCompletadas();


    // ¿Cuántas tareas ha realizado cada usuario por sector?
    @Query(value = """
    SELECT 
        u.nombre_usuario,
        s.nombre AS sector,
        COUNT(*) AS cantidad_tareas_completadas
    FROM 
        tareas t
    JOIN 
        usuarios u ON t.id_usuario = u.id_usuario
    JOIN 
        sectores s ON t.id_sector = s.id_sector
    WHERE 
        LOWER(t.estado) = 'completada'
    GROUP BY 
        u.nombre_usuario, s.nombre
    ORDER BY 
        u.nombre_usuario, s.nombre
    """, nativeQuery = true)
    List<Object[]> findCantidadTareasPorUsuarioPorSector();


}

