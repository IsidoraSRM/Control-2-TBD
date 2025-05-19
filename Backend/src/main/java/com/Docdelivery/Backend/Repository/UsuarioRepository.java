package com.Docdelivery.Backend.Repository;

import com.Docdelivery.Backend.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para mapear los resultados de la consulta a UsuarioEntity
    private static class UsuarioRowMapper implements RowMapper<UsuarioEntity> {
        @Override
        public UsuarioEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setIdUsuario(rs.getLong("id_usuario"));
            usuario.setRut(rs.getString("rut"));
            usuario.setNameParam(rs.getString("name_param"));
            usuario.setEmail(rs.getString("email"));
            usuario.setPhone(rs.getString("phone"));
            usuario.setBirthdate(rs.getDate("birthdate"));
            usuario.setPassword(rs.getString("password"));
            usuario.setRole(rs.getString("role"));
            return usuario;
        }
    }

    // Método para encontrar un usuario por email utilizando Optional
    public Optional<UsuarioEntity> findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new UsuarioRowMapper(), email));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty(); // Retorna vacío en caso de no encontrar resultados
        }
    }

    // Método para encontrar el ID de un usuario por email utilizando Optional
    public Optional<Long> findIdByEmail(String email) {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, email));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Método para insertar un nuevo usuario
    public int save(UsuarioEntity usuario) {
        String sql = "INSERT INTO usuarios (rut, name_param, email, phone, birthdate, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, usuario.getRut(), usuario.getNameParam(), usuario.getEmail(), usuario.getPhone(),
                usuario.getBirthdate(), usuario.getPassword(), usuario.getRole());
    }

    // Método para obtener todos los usuarios
    public List<UsuarioEntity> findAll() {
        String sql = "SELECT * FROM usuarios";
        return jdbcTemplate.query(sql, new UsuarioRowMapper());
    }
}