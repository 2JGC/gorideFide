package com.goridemoto.service.impl;

import com.goridemoto.domain.Motocicleta;
import com.goridemoto.repository.MotocicletaRepository;
import com.goridemoto.service.MotocicletaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MotocicletaServiceImpl implements MotocicletaService {

    private final MotocicletaRepository motocicletaRepository;

    public MotocicletaServiceImpl(MotocicletaRepository motocicletaRepository) {
        this.motocicletaRepository = motocicletaRepository;
    }

    @Override
    public List<Motocicleta> listarDisponibles() {
        return motocicletaRepository.findByDisponibleTrue();
    }

    @Override
    public Motocicleta buscarPorId(Long id) {
        return motocicletaRepository.findById(id).orElse(null);
    }

    @Override
    public void guardar(Motocicleta motocicleta) {
        motocicletaRepository.save(motocicleta);
    }
}
