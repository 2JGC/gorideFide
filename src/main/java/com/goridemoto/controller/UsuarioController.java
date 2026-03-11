package com.goride.controller;

import com.goride.model.Usuario;
import com.goride.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        usuarioRepository.save(usuario);

        model.addAttribute("mensaje", "Usuario registrado correctamente");

        return "registro";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {

            session.setAttribute("usuario", usuario.getNombre());

            return "redirect:/";
        }

        model.addAttribute("mensaje", "Email o contraseña incorrectos");

        return "login";
    }
    @GetMapping("/logout")
public String logout(HttpSession session) {

    session.invalidate(); // borra la sesión

    return "redirect:/login";
}
}
