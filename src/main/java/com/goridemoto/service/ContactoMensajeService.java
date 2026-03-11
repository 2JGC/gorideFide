package com.goridemoto.service;

import com.goridemoto.domain.ContactoMensaje;
import java.util.List;

public interface ContactoMensajeService {
    void guardar(ContactoMensaje contactoMensaje);
    List<ContactoMensaje> listar();
}