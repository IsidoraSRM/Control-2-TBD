package com.tbd_g3.backend_c2.service;

import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import com.tbd_g3.backend_c2.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        UsuarioEntity usuario = usuarioOpt.get();
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena()) // Asegúrate de que esté encriptada
                .roles(usuario.getRol()) // Asignar rol de usuario
                .build();
    }
}
