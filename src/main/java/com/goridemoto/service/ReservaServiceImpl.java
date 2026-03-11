package com.goridemoto.service;

import com.goridemoto.domain.Reserva;
import com.goridemoto.repository.ReservaRepository;
import com.goridemoto.service.ReservaService;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void guardar(Reserva reserva) {
        reservaRepository.save(reserva);
    }
}