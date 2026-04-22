package com.goridemoto.service;

public interface PasswordResetService {
    void solicitarRecuperacion(String email, String baseUrl);
    boolean tokenValido(String token);
    boolean actualizarPassword(String token, String password);
}
