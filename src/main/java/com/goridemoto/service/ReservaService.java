package com.goridemoto.service;

import com.goridemoto.domain.Reserva;
import java.util.List;

public interface ReservaService {
    void guardar(Reserva reserva);
    List<Reserva> listar();
    void actualizarEstado(Long reservaId, String estado);
    List<Reserva> listarPorUsuario(String emailUsuario);
}
