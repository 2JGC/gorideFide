package com.goridemoto.service;

import com.goridemoto.domain.PublicacionGaleria;
import java.util.List;

public interface PublicacionGaleriaService {
    List<PublicacionGaleria> listar();
    void guardar(PublicacionGaleria publicacion);
}