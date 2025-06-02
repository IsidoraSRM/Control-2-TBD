package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.*;
import com.tbd_g3.backend_c2.entity.SectorEntity;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.repository.TareaRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    // Obtener todas las tareas
    public List<TareaDTO> getAllTareas() {
        return tareaRepository.findAll().stream()
                .map(TareaDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener una tarea por ID
    public TareaEntity getTareaById(int id) {
        return tareaRepository.findById(id).orElse(null);
    }

    // Crear una nueva tarea
    public TareaEntity createTarea(TareaDTO tareaDTO) {
        TareaEntity tarea = new TareaEntity();
        mapDtoToEntity(tareaDTO, tarea);
        return tareaRepository.save(tarea);
    }

    // Actualizar una tarea existente
    public TareaEntity updateTarea(int id, TareaDTO tareaDTO) throws Exception {
        TareaEntity tareaExistente = getTareaById(id);
        if (tareaExistente == null) {
            throw new Exception("Tarea no encontrada");
        }
        mapDtoToEntity(tareaDTO, tareaExistente);
        return tareaRepository.save(tareaExistente);
    }

    // Mapear DTO a Entity
    private void mapDtoToEntity(TareaDTO dto, TareaEntity entity) {
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechavencimiento(dto.getFechavencimiento());
        entity.setEstado(dto.getEstado());

        if (dto.getLat() != 0 && dto.getLng() != 0) {
            Point point = geometryFactory.createPoint(new Coordinate(dto.getLng(), dto.getLat()));
            entity.setLocalizacion(point);
        }

        entity.setIdusuario(dto.getIdusuario());
        entity.setIdsector(dto.getIdsector());
    }

    // Eliminar una tarea
    public void deleteTarea(int id) throws Exception {
        if (!tareaRepository.existsById(id)) {
            throw new Exception("Tarea no encontrada");
        }
        tareaRepository.deleteById(id);
    }
    //QUERY 1; ¿Cuántas tareas ha hecho el usuario por sector?
    public List<TareasPorUsuarioSectorDTO> getTareasCompletadasPorUsuarioYSector() {
        return tareaRepository.countTareasCompletadasPorUsuarioYSector().stream()
                .map(result -> new TareasPorUsuarioSectorDTO(
                        (String) result[0],
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    //completar la tarea
    public void completarTarea(int id) throws Exception {
        TareaEntity tarea = tareaRepository.findById(Integer.valueOf(id)).orElse(null);
        if (tarea != null) {
            try {
                tarea.setEstado("COMPLETADA");
                tareaRepository.save(tarea);
            } catch (Exception e) {
                throw new Exception("Error al completar la tarea");
            }
        }
    }

    public List<TareaFiltradaDTO> filtrarTareas(String estado, String palabraClave) {
        List<Object[]> resultados = tareaRepository.filtrarTareas(
                estado != null && !estado.trim().isEmpty() ? estado : null,
                palabraClave != null && !palabraClave.trim().isEmpty() ? palabraClave : null
        );

        return resultados.stream()
                .map(this::mapToTareaFiltradaDTO)
                .collect(Collectors.toList());
    }
    private TareaFiltradaDTO mapToTareaFiltradaDTO(Object[] resultado) {
        TareaFiltradaDTO dto = new TareaFiltradaDTO();
        dto.setIdtarea((Integer) resultado[0]);
        dto.setTitulo((String) resultado[1]);
        dto.setEstado((String) resultado[2]);
        dto.setSector((String) resultado[3]);
        // Convertir java.sql.Date a LocalDate
        java.sql.Date sqlDate = (java.sql.Date) resultado[4];
        dto.setFechaVencimiento(sqlDate != null ? sqlDate.toLocalDate() : null);

        return dto;
    }

    public TareaCercanaDTO encontrarTareaMasCercana(Integer idUsuario) {
        List<Object[]> resultados = tareaRepository.findTareaMasCercana(idUsuario);

        if (resultados == null || resultados.isEmpty()) {
            return null;
        }

        Object[] resultado = resultados.get(0);
        TareaCercanaDTO dto = new TareaCercanaDTO();
        dto.setIdTarea((Integer) resultado[0]);
        dto.setTitulo((String) resultado[1]);
        dto.setNombreSector((String) resultado[2]);

        // La conversión del punto
        if (resultado[3] != null) {
            dto.setLocalizacion((org.geolatte.geom.Point) resultado[3]);
        }

        dto.setDistanciaMetros(Double.valueOf(((Number) resultado[4]).doubleValue()));
        dto.setFechaVencimiento(((java.sql.Date) resultado[5]).toLocalDate());

        return dto;
    }

    // ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes?
    public List<SectorPendientesDTO> obtenerSectoresConMasTareasPendientes() {
        List<Object[]> results = tareaRepository.findSectoresConMasTareasPendientesConCantidad();
        List<SectorPendientesDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            SectorPendientesDTO dto = new SectorPendientesDTO();
            dto.setIdsector(((Number) row[0]).intValue());
            dto.setNombre((String) row[1]);
            dto.setDescripcion((String) row[2]);
            dto.setCantidadTareasPendientes(((Number) row[3]).intValue());
            dtos.add(dto);
        }
        return dtos;
    }

    // ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?
    public Object[] encontrarTareaPendienteMasCercana(Integer idUsuario) {
        List<Object[]> resultados = tareaRepository.findTareaPendienteMasCercana(idUsuario);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    //  ¿Cuál es el promedio de distancia entre las tareas completadas y el punto registrado del usuario?
    public Object[] obtenerPromedioDistanciaTareasCompletadas(Integer idUsuario) {
        List<Object[]> resultados = tareaRepository.findPromedioDistanciaTareasCompletadas(idUsuario);
        return resultados.isEmpty() ? null : resultados.get(0);
    }
    // ¿Cuál es el promedio global de distancia de las tareas completadas respecto a la ubicación de los usuarios?
    public Double obtenerPromedioGlobalDistanciaTareasCompletadas() {
        return tareaRepository.findPromedioGlobalDistanciaTareasCompletadas();
    }
    // ¿Cuántas tareas ha realizado cada usuario por sector?
    public List<Object[]> obtenerCantidadTareasPorUsuarioPorSector() {
        return tareaRepository.findCantidadTareasPorUsuarioPorSector();
    }
}
