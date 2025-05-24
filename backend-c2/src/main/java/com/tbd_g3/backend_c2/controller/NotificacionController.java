package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.entity.NotificacionEntity;
import com.tbd_g3.backend_c2.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notificaciones")
@CrossOrigin("*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }


    @PostMapping("/crear")
    public ResponseEntity<NotificacionEntity> crearNotificacion(@RequestBody NotificacionEntity notificacion) {
        return ResponseEntity.ok(notificacionService.crearNotificacion(notificacion));
    }
}
