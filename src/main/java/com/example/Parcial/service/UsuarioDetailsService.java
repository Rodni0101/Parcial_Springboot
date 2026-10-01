package com.example.Parcial.service;

import com.example.Parcial.model.Usuario;
import com.example.Parcial.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese nombre de usuario"));

        return User.withUsername(usuario.getNombreUsuario())
                .password(usuario.getContrasena())
                .roles(usuario.getRol().getNombre())
                .build();
    }
}
