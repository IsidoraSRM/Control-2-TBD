package com.tbd_g3.backend_c2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorPendientesDTO {
    private Integer idsector;
    private String nombre;
    private String descripcion;
    private Integer cantidadTareasPendientes;
}