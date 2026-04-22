package com.goridemoto.controller;

import com.goridemoto.domain.Usuario;
import com.goridemoto.service.ReservaService;
import com.goridemoto.service.UsuarioService;
import java.security.Principal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    public PerfilController(UsuarioService usuarioService, ReservaService reservaService) {
        this.usuarioService = usuarioService;
        this.reservaService = reservaService;
    }

    private String getEmail(Principal principal) {
        // Soporta login con email/password Y OAuth2 (Meta/Facebook)
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = ((OAuth2AuthenticationToken) principal).getPrincipal();
            return oauthUser.getAttribute("email");
        }
        return principal.getName();
    }

    @GetMapping
    public String verPerfil(Model model, Principal principal) {
        String email = getEmail(principal);
        Usuario usuario = usuarioService.buscarPorEmail(email);
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping
    public String actualizarPerfil(@RequestParam("nombre") String nombre,
                                   Principal principal,
                                   RedirectAttributes ra) {
        String email = getEmail(principal);
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuarioService.actualizarPerfil(usuario);
        }
        ra.addFlashAttribute("exito", "Profile updated successfully.");
        return "redirect:/perfil";
    }

    @GetMapping("/historial")
    public String verHistorial(Model model, Principal principal) {
        String email = getEmail(principal);
        model.addAttribute("reservas", reservaService.listarPorUsuario(email));
        return "historial";
    }
}
