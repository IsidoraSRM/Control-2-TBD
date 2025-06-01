package com.tbd_g3.backend_c2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TareasPorUsuarioSectorDTO {
    private String nombreUsuario;
    private String nombreSector;
    private Long cantidadTareas;
}