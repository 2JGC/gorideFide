package com.goridemoto.controller;

import com.goridemoto.domain.Usuario;
import com.goridemoto.repository.UsuarioRepository;
import com.goridemoto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public RegistroController(UsuarioService usuarioService,
                              UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registrar(@ModelAttribute("usuario") Usuario usuario,
                            RedirectAttributes redirectAttributes) {

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("errorRegistro", "This email is already registered.");
            return "redirect:/register";
        }

        usuarioService.registrarUsuario(usuario);
        redirectAttributes.addFlashAttribute("registroExitoso", "Account created successfully. You can now log in.");
        return "redirect:/login";
    }
}