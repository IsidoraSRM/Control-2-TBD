package com.tbd_g3.backend_c2.repository;

import com.tbd_g3.backend_c2.entity.SectorEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectorRepository extends JpaRepository<SectorEntity, Integer> {

    @Query(value = """
        SELECT s FROM SectorEntity s 
        JOIN TareaEntity t ON t.idsector = s.idsector
        WHERE t.estado = 'COMPLETADA' 
        AND function('ST_DWithin', s.localizacion, :userLocation, 2000) = true
        GROUP BY s.idsector
        ORDER BY COUNT(t.idtarea) DESC
        LIMIT 1
    """)
    SectorEntity findSectorWithMostCompletedTasks(@Param("userLocation") Point userLocation);


    @Query(value = """
        SELECT s FROM SectorEntity s 
        JOIN TareaEntity t ON t.idsector = s.idsector
        WHERE t.estado = 'COMPLETADA' 
        AND function('ST_DWithin', s.localizacion, :userLocation, 5000) = true
        GROUP BY s.idsector
        ORDER BY COUNT(t.idtarea) DESC
        LIMIT 1
    """)
    SectorEntity buscarSectorConMasTareasCompletadasEn5km(@Param("userLocation") Point userLocation);

}

