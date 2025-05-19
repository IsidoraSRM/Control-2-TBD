package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.DetallePedidoEntity;
import com.Docdelivery.Backend.Service.DetallePedidoService;
import com.Docdelivery.Backend.dto.ServiciosDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detallepedidos")
@CrossOrigin(origins = "*")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    // Create
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<Void> createDetallePedido(@RequestBody DetallePedidoEntity detalle) {
        try {
            detallePedidoService.save(detalle);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read - Get all
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<List<DetallePedidoEntity>> getAllDetalles() {
        List<DetallePedidoEntity> detalles = detallePedidoService.findAll();
        return ResponseEntity.ok(detalles);
    }

    // Read - Get by pedido ID
    @GetMapping("/pedido/{idPedido}")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<List<DetallePedidoEntity>> getDetallesByPedidoId(@PathVariable Long idPedido) {
        List<DetallePedidoEntity> detalles = detallePedidoService.findByPedidoId(idPedido);
        if (detalles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detalles);
    }

    // Update
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> updateDetallePedido(
            @PathVariable Long id,
            @RequestBody DetallePedidoEntity detalle) {
        detalle.setIdDetallePedido(id);
        boolean updated = detallePedidoService.update(detalle);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id) {
        boolean deleted = detallePedidoService.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get servicios más pedidos (existing endpoint)
    @GetMapping("/masPedidos")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN"})
    public ResponseEntity<List<ServiciosDto>> getServiciosMasPedidos() {
        return ResponseEntity.ok(detallePedidoService.obtenerServiciosMasPedidos());
    }
}