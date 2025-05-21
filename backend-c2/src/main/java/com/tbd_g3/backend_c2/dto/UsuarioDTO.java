package com.tbd_g3.backend_c2.dto;

import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UsuarioDTO {
    private int idusuario;
    private String nombreusuario;
    private String correo;
    private double lat;
    private double lng;

    public UsuarioDTO(UsuarioEntity usuario) {
        this.idusuario = usuario.getIdUsuario();
        this.nombreusuario = usuario.getNombreUsuario();
        this.correo = usuario.getCorreo();

        if (usuario.getLocalizacion() != null) {
            this.lat = usuario.getLocalizacion().getY(); // Latitud
            this.lng = usuario.getLocalizacion().getX(); // Longitud
        }
    }
}
