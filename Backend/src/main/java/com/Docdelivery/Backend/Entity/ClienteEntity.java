package com.Docdelivery.Backend.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    private Long clienteId;
    private String nombre;
    private String direccion;
    private String email;
    private String telefono;

}
