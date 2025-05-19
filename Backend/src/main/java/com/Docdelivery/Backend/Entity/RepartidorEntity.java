package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepartidorEntity {
    private Long repartidorId;
    private String nombre;
    private String telefono;
    private Boolean disponible;

}
