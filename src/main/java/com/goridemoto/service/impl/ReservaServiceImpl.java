package com.goridemoto.service.impl;

import com.goridemoto.domain.Reserva;
import com.goridemoto.repository.ReservaRepository;
import com.goridemoto.service.ReservaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override public void guardar(Reserva reserva) { reservaRepository.save(reserva); }
    @Override public List<Reserva> listar() { return reservaRepository.findAll(); }

    @Override
    public List<Reserva> listarPorUsuario(String emailUsuario) {
        return reservaRepository.findByEmailUsuarioOrderByFechaCreacionDesc(emailUsuario);
    }

    @Override
    public void actualizarEstado(Long reservaId, String estado) {
        Reserva reserva = reservaRepository.findById(reservaId).orElse(null);
        if (reserva == null) return;
        reserva.setEstado(estado);
        reservaRepository.save(reserva);
    }
}
