package com.goridemoto.controller;
import com.goridemoto.service.PasswordResetService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final PasswordResetService passwordResetService;

    public LoginController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/forgot-password")
    public String mostrarOlvidePassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String solicitarRecuperacion(@RequestParam("email") String email,
                                        HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.solicitarRecuperacion(email, obtenerBaseUrl(request));
        } catch (Exception ex) {
            logger.warn("No se pudo procesar la recuperacion de contrasena", ex);
            redirectAttributes.addFlashAttribute("error",
                    "We could not send the reset email right now. Please try again later.");
            return "redirect:/forgot-password";
        }

        redirectAttributes.addFlashAttribute("mensaje",
                "If that email exists in our system, we sent password reset instructions.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String mostrarResetPassword(@RequestParam("token") String token,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        if (!passwordResetService.tokenValido(token)) {
            redirectAttributes.addFlashAttribute("error",
                    "The password reset link is invalid or expired.");
            return "redirect:/forgot-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String actualizarPassword(@RequestParam("token") String token,
                                     @RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }

        if (!passwordResetService.actualizarPassword(token, password)) {
            redirectAttributes.addFlashAttribute("error",
                    "The password reset link is invalid, expired, or the password is too short.");
            return "redirect:/forgot-password";
        }

        redirectAttributes.addFlashAttribute("passwordActualizado",
                "Password updated successfully. You can now log in.");
        return "redirect:/login";
    }

    private String obtenerBaseUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.replace(request.getRequestURI(), request.getContextPath());
    }
}
