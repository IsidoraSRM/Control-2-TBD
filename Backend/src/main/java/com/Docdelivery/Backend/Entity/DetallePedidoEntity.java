package com.Docdelivery.Backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoEntity {
    public Long idDetallePedido;
    // DP oneToOne P
    public Long idPedido;
    // DP oneToMany S
    public Long idServicio;
    //public Long productoId; // esto va?
    public Integer cantidad;
    public String direccionsDestino;
    public String direccionsInicio;
}
