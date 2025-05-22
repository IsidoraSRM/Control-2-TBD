package com.tbd_g3.backend_c2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaFiltradaDTO {
    private Integer idtarea;
    private String titulo;
    private String estado;
    private String sector;
    private LocalDate fechaVencimiento;
}