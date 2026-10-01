package com.example.Parcial.repository;

import com.example.Parcial.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    boolean existsByCorreo(String correo);
    boolean existsByNombreUsuario(String nombreUsuario);
}
