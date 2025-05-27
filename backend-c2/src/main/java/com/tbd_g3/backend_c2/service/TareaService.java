package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.TareaCercanaDTO;
import com.tbd_g3.backend_c2.dto.TareaDTO;
import com.tbd_g3.backend_c2.dto.TareaFiltradaDTO;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.repository.TareaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    //obtener todas las tareas
    public List<TareaDTO> getAllTareas() {
        List<TareaEntity> tareas = tareaRepository.findAll();
        return tareas.stream().map(TareaDTO::new).collect(Collectors.toList());
    }

    //obtener 1 tarea por id
    public TareaEntity getTareaById(int id) {
        return tareaRepository.findById(id).orElse(null);
    }

    //crear una tarea
    public void saveTarea(TareaEntity tarea) {
        tareaRepository.save(tarea);
    }

    //actualizar la tarea
    public void updateTarea(TareaEntity tarea) throws Exception {
        try {
            tareaRepository.save(tarea);
        } catch (Exception e) {
            throw new Exception("Error al atualizar la tarea");
        }
    }

    //eliminar la tarea
    public void deleteTarea(int id) throws Exception {
        try {
            tareaRepository.delete(tareaRepository.findById(id).orElse(null));
        } catch (Exception e) {
            throw new Exception("Error al eliminar la tarea");
        }
    }

    //completar la tarea
    public void completarTarea(int id) throws Exception {
        TareaEntity tarea = tareaRepository.findById(id).orElse(null);
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

        dto.setDistanciaMetros(((Number) resultado[4]).doubleValue());
        dto.setFechaVencimiento(((java.sql.Date) resultado[5]).toLocalDate());

        return dto;
    }

    // ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes?
    public List<Object[]> obtenerSectoresConMasTareasPendientes() {
        return tareaRepository.findSectoresConMasTareasPendientes();
    }

    // ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?
    public Object[] encontrarTareaPendienteMasCercana(Integer idUsuario) {
        List<Object[]> resultados = tareaRepository.findTareaPendienteMasCercana(idUsuario);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    //
    public Object[] obtenerPromedioDistanciaTareasCompletadas(Integer idUsuario) {
        List<Object[]> resultados = tareaRepository.findPromedioDistanciaTareasCompletadas(idUsuario);
        return resultados.isEmpty() ? null : resultados.get(0);
    }



}
