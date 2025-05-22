package com.tbd_g3.backend_c2.controller;

import com.tbd_g3.backend_c2.config.JwtUtil;
import com.tbd_g3.backend_c2.dto.LoginDto;
import com.tbd_g3.backend_c2.dto.RegisterDto;
import com.tbd_g3.backend_c2.entity.UsuarioEntity;
import com.tbd_g3.backend_c2.repository.UsuarioRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 📌 Autenticación con JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Optional<UsuarioEntity> optionalUser = usuarioRepository.findByCorreo(loginDto.getCorreo());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo no está registrado. Por favor, regístrese primero.");
        }

        UsuarioEntity user = optionalUser.get();
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(loginDto.getCorreo(), loginDto.getContrasena());

        try {
            authenticationManager.authenticate(loginToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        String jwt = jwtUtil.createToken(loginDto.getCorreo(), user.getRol());
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("userId", user.getIdUsuario());
        response.put("role", user.getRol());
        response.put("ubicacion", user.getLocalizacion().toString());

        return ResponseEntity.ok(response);
    }

    // 📍 Registro de usuario con ubicación geoespacial
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        Optional<UsuarioEntity> foundUser = usuarioRepository.findByCorreo(registerDto.getCorreo());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
        }

        if (registerDto.getContrasena() == null || registerDto.getContrasena().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña no puede estar vacía.");
        }

        try {
            String hashedPassword = passwordEncoder.encode(registerDto.getContrasena());

            // 📍 Convertir latitud/longitud en `Point` para PostGIS
            Point ubicacion = geometryFactory.createPoint(new Coordinate(registerDto.getLongitud(), registerDto.getLatitud()));
            ubicacion.setSRID(4326); // Definir sistema de referencia espacial

            UsuarioEntity newUser = new UsuarioEntity();
            newUser.setNombreUsuario(registerDto.getNombreUsuario());
            newUser.setContrasena(hashedPassword);
            newUser.setCorreo(registerDto.getCorreo());
            newUser.setFechaRegistro(LocalDateTime.now());
            newUser.setRol(registerDto.getRol());
            newUser.setLocalizacion(ubicacion);

            usuarioRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito con ubicación geoespacial.");
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno en el servidor.");
        }
    }
}

