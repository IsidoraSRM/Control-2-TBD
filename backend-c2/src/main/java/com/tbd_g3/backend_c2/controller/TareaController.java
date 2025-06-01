package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.*;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.service.TareaService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    // Obtener todas las tareas
    @GetMapping
    public List<TareaDTO> getAllTareas() {
        return tareaService.getAllTareas();
    }

    // Obtener una tarea por ID
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> getTareaById(@PathVariable int id) {
        TareaEntity tarea = tareaService.getTareaById(id);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TareaDTO(tarea));
    }

    // Crear una nueva tarea (con RequestBody en lugar de parámetros)
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR", "ROLE_CLIENTE"})
    public ResponseEntity<TareaDTO> createTarea(@RequestBody TareaDTO tareaDTO) {
        TareaEntity tarea = tareaService.createTarea(tareaDTO);
        return ResponseEntity.ok(new TareaDTO(tarea));
    }

    // Actualizar una tarea existente (con RequestBody completo)
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> updateTarea(@PathVariable int id, @RequestBody TareaDTO tareaDTO) {
        try {
            TareaEntity updatedTarea = tareaService.updateTarea(id, tareaDTO);
            return ResponseEntity.ok(new TareaDTO(updatedTarea));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarea(@PathVariable int id) {
        try {
            tareaService.deleteTarea(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //QUERY 1; ¿Cuántas tareas ha hecho el usuario por sector?
    @GetMapping("/tareas-por-usuario-sector")
    public ResponseEntity<List<TareasPorUsuarioSectorDTO>> getTareasCompletadasPorUsuarioYSector() {
        List<TareasPorUsuarioSectorDTO> resultados = tareaService.getTareasCompletadasPorUsuarioYSector();
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @PutMapping("/completar/{id}")
    public void completarTarea(@PathVariable int id) throws Exception {
        try{
            tareaService.completarTarea(id);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
    // filtros
    @GetMapping("/filtrar")
    public ResponseEntity<List<TareaFiltradaDTO>> filtrarTareas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false, name = "palabraClave") String palabraClave) {

        List<TareaFiltradaDTO> tareas = tareaService.filtrarTareas(estado, palabraClave);
        return ResponseEntity.ok(tareas);
    }
    //Consulta 2: buscar la tareas mas cercanas al usuario
    @GetMapping("/cercana/{idUsuario}")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR", "ROLE_CLIENTE"})
    public ResponseEntity<TareaCercanaDTO> getTareaMasCercana(@PathVariable Integer idUsuario) {
        TareaCercanaDTO tarea = tareaService.encontrarTareaMasCercana(idUsuario);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarea);
    }

    @GetMapping("/sectores-pendientes")
    public ResponseEntity<List<SectorDTO>> getSectoresConMasTareasPendientes() {
        List<SectorDTO> sectores = tareaService.obtenerSectoresConMasTareasPendientes();
        if (sectores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sectores);
    }

    @GetMapping("/pendiente-cercana/{idUsuario}")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR", "ROLE_CLIENTE"})
    public ResponseEntity<Object[]> getTareaPendienteMasCercana(@PathVariable Integer idUsuario) {
        Object[] tarea = tareaService.encontrarTareaPendienteMasCercana(idUsuario);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarea);
    }

    @GetMapping("/promedio-distancia/{idUsuario}")
    @Secured({"ROLE_ADMIN", "ROLE_TRABAJADOR", "ROLE_CLIENTE"})
    public ResponseEntity<Object[]> getPromedioDistanciaTareasCompletadas(@PathVariable Integer idUsuario) {
        Object[] promedio = tareaService.obtenerPromedioDistanciaTareasCompletadas(idUsuario);
        if (promedio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/promedio-global-distancia-tareas-completadas")
    public ResponseEntity<Double> getPromedioGlobalDistanciaTareasCompletadas() {
        Double promedio = tareaService.obtenerPromedioGlobalDistanciaTareasCompletadas();
        if (promedio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/cantidad-tareas-por-usuario-por-sector")
    public ResponseEntity<List<Object[]>> getCantidadTareasPorUsuarioPorSector() {
        List<Object[]> resultados = tareaService.obtenerCantidadTareasPorUsuarioPorSector();
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

}