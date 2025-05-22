package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.dto.TareaDTO;
import com.tbd_g3.backend_c2.entity.TareaEntity;
import com.tbd_g3.backend_c2.repository.TareaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        try{
            tareaRepository.save(tarea);
        }
        catch(Exception e){
            throw new Exception("Error al atualizar la tarea");
        }
    }
    //eliminar la tarea
    public void deleteTarea(int id) throws Exception {
        try {
            tareaRepository.delete(tareaRepository.findById(id).orElse(null));
        }
        catch(Exception e){
            throw new Exception("Error al eliminar la tarea");
        }
    }
    //completar la tarea
    public void completarTarea(int id) throws Exception {
        TareaEntity tarea = tareaRepository.findById(id).orElse(null);
        if(tarea != null){
            try{
                tarea.setEstado("COMPLETADA");
                tareaRepository.save(tarea);
            }catch (Exception e){
                throw new Exception("Error al completar la tarea");
            }
        }
    }
}
