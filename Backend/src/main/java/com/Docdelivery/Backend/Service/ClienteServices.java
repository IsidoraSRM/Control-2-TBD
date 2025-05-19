package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.ClienteEntity;
import com.Docdelivery.Backend.Repository.ClienteRepository;
import com.Docdelivery.Backend.dto.ClienteConTotalGastadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServices {
    @Autowired
    private ClienteRepository clienteRepository;

    //consulta numero 1
    public Optional<ClienteConTotalGastadoDTO> obtenerClienteMasAGastado() {return clienteRepository.obtenerClienteQueMasHaGastado();}


    //cliente por idUsuario
    public Optional<Long> findClienteIdByUsuarioId(Long usuarioId) {return clienteRepository.findClienteIdByUsuarioId(usuarioId);}

    public void actualizarCliente(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id).get();
        clienteRepository.update(cliente);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.removeCliente(id);
    }
}
