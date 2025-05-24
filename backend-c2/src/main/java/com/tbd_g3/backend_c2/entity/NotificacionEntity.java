package com.tbd_g3.backend_c2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion")
    private Long idNotificacion;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fechaEnvio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Column(name = "leida", nullable = false)
    private boolean leida = false;

    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @Column(name = "idTarea")
    private Long idTarea;

}