package com.goridemoto.service;

import com.goridemoto.domain.Reserva;

public interface EmailService {
    void enviarRecuperacionPassword(String destinatario, String nombre, String resetUrl);
    void enviarAgradecimientoReserva(Reserva reserva);
}
