package com.goridemoto.service;

import com.goridemoto.domain.Motocicleta;
import java.util.List;

public interface MotocicletaService {
    List<Motocicleta> listarDisponibles();
    Motocicleta buscarPorId(Long id);
    void guardar(Motocicleta motocicleta);
}