package com.example.Parcial.config;

import com.example.Parcial.model.Rol;
import com.example.Parcial.model.Usuario;
import com.example.Parcial.repository.RolRepository;
import com.example.Parcial.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeRolesAndAdmin(RolRepository rolRepository,
                                               UsuarioRepository usuarioRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            Rol admin = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> rolRepository.save(new Rol("ADMIN")));
            rolRepository.findByNombre("USER")
                    .orElseGet(() -> rolRepository.save(new Rol("USER")));

            Usuario administrador = usuarioRepository.findByCorreo("admin123@gmail.com")
                    .orElseGet(Usuario::new);
            administrador.setNombreUsuario("admin123");
            administrador.setCorreo("admin123@gmail.com");
            administrador.setContrasena(passwordEncoder.encode("contraseña12345678"));
            administrador.setRol(admin);
            usuarioRepository.save(administrador);
        };
    }
}
