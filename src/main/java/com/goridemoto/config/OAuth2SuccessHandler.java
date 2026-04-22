package com.goridemoto.config;

import com.goridemoto.domain.Usuario;
import com.goridemoto.repository.UsuarioRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public OAuth2SuccessHandler(UsuarioRepository usuarioRepository,
                                BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String nombre = oauthUser.getAttribute("name");

        if (email != null) {
            Optional<Usuario> existente = usuarioRepository.findByEmail(email);

            if (existente.isEmpty()) {
                // Primera vez: crear usuario automáticamente
                Usuario nuevo = new Usuario();
                nuevo.setEmail(email);
                nuevo.setNombre(nombre != null ? nombre : "");
                nuevo.setRol("USER");
                // Contraseña aleatoria (no la va a usar porque inicia con OAuth2)
                nuevo.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                usuarioRepository.save(nuevo);
            }
        }

        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
