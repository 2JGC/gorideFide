package com.goridemoto.service.impl;

import com.goridemoto.domain.ContactoMensaje;
import com.goridemoto.repository.ContactoMensajeRepository;
import com.goridemoto.service.ContactoMensajeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContactoMensajeServiceImpl implements ContactoMensajeService {

    private final ContactoMensajeRepository contactoMensajeRepository;

    public ContactoMensajeServiceImpl(ContactoMensajeRepository contactoMensajeRepository) {
        this.contactoMensajeRepository = contactoMensajeRepository;
    }

    @Override
    public void guardar(ContactoMensaje contactoMensaje) {
        contactoMensajeRepository.save(contactoMensaje);
    }

    @Override
    public List<ContactoMensaje> listar() {
        return contactoMensajeRepository.findAll();
    }
}
