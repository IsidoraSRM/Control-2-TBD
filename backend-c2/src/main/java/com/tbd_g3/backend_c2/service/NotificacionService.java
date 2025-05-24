package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.entity.NotificacionEntity;
import com.tbd_g3.backend_c2.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionEntity crearNotificacion(NotificacionEntity notificacion) {
        return notificacionRepository.save(notificacion);
    }
}
