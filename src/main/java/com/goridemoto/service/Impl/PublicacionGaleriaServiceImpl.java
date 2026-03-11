package com.goridemoto.service.impl;

import com.goridemoto.domain.PublicacionGaleria;
import com.goridemoto.repository.PublicacionGaleriaRepository;
import com.goridemoto.service.PublicacionGaleriaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PublicacionGaleriaServiceImpl implements PublicacionGaleriaService {

    private final PublicacionGaleriaRepository repository;

    public PublicacionGaleriaServiceImpl(PublicacionGaleriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PublicacionGaleria> listar() {
        return repository.findAll();
    }

    @Override
    public void guardar(PublicacionGaleria publicacion) {
        repository.save(publicacion);
    }
}