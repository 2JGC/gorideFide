package com.goridemoto.service.impl;

import com.goridemoto.domain.Usuario;
import com.goridemoto.repository.UsuarioRepository;
import com.goridemoto.service.EmailService;
import com.goridemoto.service.PasswordResetService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int MINUTOS_EXPIRACION = 30;

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetServiceImpl(UsuarioRepository usuarioRepository,
                                    BCryptPasswordEncoder passwordEncoder,
                                    EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void solicitarRecuperacion(String email, String baseUrl) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        Usuario usuario = usuarioRepository.findByEmail(email.trim()).orElse(null);
        if (usuario == null) {
            return;
        }

        String token = UUID.randomUUID().toString();
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordTokenExpiration(LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION));
        usuarioRepository.save(usuario);

        String resetUrl = baseUrl + "/reset-password?token=" + token;
        emailService.enviarRecuperacionPassword(usuario.getEmail(), usuario.getNombre(), resetUrl);
    }

    @Override
    public boolean tokenValido(String token) {
        Usuario usuario = buscarUsuarioConTokenVigente(token);
        return usuario != null;
    }

    @Override
    @Transactional
    public boolean actualizarPassword(String token, String password) {
        Usuario usuario = buscarUsuarioConTokenVigente(token);
        if (usuario == null || password == null || password.trim().length() < 6) {
            return false;
        }

        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiration(null);
        usuarioRepository.save(usuario);
        return true;
    }

    private Usuario buscarUsuarioConTokenVigente(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioRepository.findByResetPasswordToken(token.trim()).orElse(null);
        if (usuario == null || usuario.getResetPasswordTokenExpiration() == null) {
            return null;
        }

        if (usuario.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now())) {
            return null;
        }

        return usuario;
    }
}
