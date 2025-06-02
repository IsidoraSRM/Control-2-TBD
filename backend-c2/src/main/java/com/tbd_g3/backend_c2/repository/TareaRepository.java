package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.SectorEntity;
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

    //QUERY 1; ¿Cuántas tareas ha hecho el usuario por sector?

    @Query(value = """
    SELECT 
        u.nombreusuario,
        s.nombre AS nombreSector,
        COUNT(t.idtarea) AS cantidadTareas
    FROM 
        tareas t
    JOIN 
        usuarios u ON t.idusuario = u.idusuario
    JOIN 
        sectores s ON t.idsector = s.idsector
    WHERE 
        t.estado = 'COMPLETADA'
    GROUP BY 
        u.nombreusuario, s.nombre
    ORDER BY 
        u.nombreusuario, COUNT(t.idtarea) DESC
    """, nativeQuery = true)
    List<Object[]> countTareasCompletadasPorUsuarioYSector();

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
            AND t.idusuario = :idUsuario
        ORDER BY 
            distancia_metros ASC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findTareaMasCercana(@Param("idUsuario") Integer idUsuario);

    // ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes?
    @Query(value = """
        SELECT s 
        FROM SectorEntity s
        WHERE s.idsector IN (
            SELECT t.idsector
            FROM TareaEntity t
            WHERE t.estado = 'PENDIENTE'
            GROUP BY t.idsector
            HAVING COUNT(t.idtarea) = (
                SELECT MAX(cantidad)
                FROM (
                    SELECT COUNT(*) AS cantidad
                    FROM TareaEntity
                    WHERE estado = 'PENDIENTE'
                    GROUP BY idsector
                ) AS subquery
            )
        )
        """)
    List<SectorEntity> findSectoresConMasTareasPendientes();

    // ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?
    @Query(value = """
        SELECT 
            u.nombreusuario,
            t.idtarea,
            t.titulo,
            s.nombre AS nombre_sector,
            ST_X(t.localizacion) AS lat_tarea,
            ST_Y(t.localizacion) AS lng_tarea,
            ST_X(u.localizacion) AS lat_usuario,
            ST_Y(u.localizacion) AS lng_usuario,
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
            ST_Distance(u.localizacion::geography, t.localizacion::geography)
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findTareaPendienteMasCercana(@Param("idUsuario") Integer idUsuario);


    // ¿Cuál es el promedio de distancia entre las tareas completadas y el punto registrado del usuario?
    @Query(value = """
        SELECT 
            u.idusuario,
            u.nombreusuario,
            AVG(ST_Distance(u.localizacion::geography, t.localizacion::geography)) AS promedio_distancia_metros
        FROM 
            tareas t
        JOIN 
            usuarios u ON u.idusuario = :idUsuario
        WHERE 
            t.estado = 'COMPLETADA'
            AND t.idusuario = u.idusuario
        GROUP BY 
            u.idusuario, u.nombreusuario
        """, nativeQuery = true)
    List<Object[]> findPromedioDistanciaTareasCompletadas(@Param("idUsuario") Integer idUsuario);


    // ¿Cuál es el promedio global de distancia de las tareas completadas respecto a la ubicación de los usuarios?
    @Query(value = """
    SELECT 
        ROUND(AVG(ST_Distance(u.localizacion::geography, t.localizacion::geography))::numeric, 2) AS promedio_distancia_metros
    FROM 
        tareas t
    JOIN 
        usuarios u ON t.idusuario = u.idusuario
    WHERE 
        LOWER(t.estado) = 'completada'
    """, nativeQuery = true)
    Double findPromedioGlobalDistanciaTareasCompletadas();


    // ¿Cuántas tareas ha realizado cada usuario por sector?
    @Query(value = """
    SELECT 
        u.nombreusuario,
        s.nombre AS sector,
        COUNT(*) AS cantidad_tareas_completadas
    FROM 
        tareas t
    JOIN 
        usuarios u ON t.idusuario = u.idusuario
    JOIN 
        sectores s ON t.idsector = s.idsector
    WHERE 
        LOWER(t.estado) = 'completada'
    GROUP BY 
        u.nombreusuario, s.nombre
    ORDER BY 
        u.nombreusuario, s.nombre
    """, nativeQuery = true)
    List<Object[]> findCantidadTareasPorUsuarioPorSector();


}

