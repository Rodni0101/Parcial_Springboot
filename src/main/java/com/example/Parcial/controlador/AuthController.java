package com.example.Parcial.controlador;

import com.example.Parcial.model.Rol;
import com.example.Parcial.model.Usuario;
import com.example.Parcial.repository.RolRepository;
import com.example.Parcial.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombreUsuario,
                            @RequestParam String correo,
                            @RequestParam String contrasena,
                            @RequestParam String confirmarContrasena,
                            RedirectAttributes redirectAttributes) {
        String nombreUsuarioNormalizado = nombreUsuario.trim().toLowerCase();
        String correoNormalizado = correo.trim().toLowerCase();
        if (nombreUsuarioNormalizado.isBlank() || !nombreUsuarioNormalizado.matches("[a-z0-9._-]{3,50}")) {
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario debe tener entre 3 y 50 caracteres y solo usar letras, números, punto, guion o guion bajo.");
            return "redirect:/registro";
        }
        if (usuarioRepository.existsByNombreUsuario(nombreUsuarioNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una cuenta con ese nombre de usuario.");
            return "redirect:/registro";
        }
        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una cuenta con ese correo.");
            return "redirect:/registro";
        }
        if (!contrasena.equals(confirmarContrasena) || contrasena.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas deben coincidir y tener al menos 8 caracteres.");
            return "redirect:/registro";
        }

        Rol rolUsuario = rolRepository.findByNombre("USER").orElseThrow();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuarioNormalizado);
        usuario.setCorreo(correoNormalizado);
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setRol(rolUsuario);
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("exito", "Cuenta creada. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }
}
