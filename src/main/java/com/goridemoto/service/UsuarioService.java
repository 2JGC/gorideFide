package com.goridemoto.service;

import com.goridemoto.domain.Usuario;

public interface UsuarioService {
    void registrarUsuario(Usuario usuario);
    Usuario buscarPorEmail(String email);
    void actualizarPerfil(Usuario usuario);
}
