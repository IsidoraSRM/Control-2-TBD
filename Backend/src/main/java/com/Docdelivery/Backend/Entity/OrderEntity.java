package com.Docdelivery.Backend.Entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    private Long idPedido;
    // C one to one P(O)
    private Long clienteId;
    // R one to many P
    private Long repartidorId;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private String estadoPedido;
    private String prioridadPedido;
}