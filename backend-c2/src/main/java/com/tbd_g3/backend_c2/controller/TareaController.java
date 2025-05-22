package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.dto.TareaCercanaDTO;
import com.tbd_g3.backend_c2.dto.TareaDTO;
import com.tbd_g3.backend_c2.dto.TareaFiltradaDTO;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public List<TareaDTO> getAllTareas() {
        return tareaService.getAllTareas();
    }

    @GetMapping("/{id}")
    public TareaEntity getTareaById(@PathVariable int id) {
        return tareaService.getTareaById(id);
    }

    @PostMapping()
    public void addTarea(@RequestBody TareaEntity tarea) {
        TareaEntity newTarea = tarea;
        tareaService.saveTarea(newTarea);
    }

    @PutMapping()
    public void updateTarea(@RequestBody TareaEntity tarea) {
        TareaEntity newTarea = tarea;
        tareaService.saveTarea(newTarea);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteTarea(@PathVariable int id) throws Exception {
        try{
            tareaService.deleteTarea(id);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
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


}

