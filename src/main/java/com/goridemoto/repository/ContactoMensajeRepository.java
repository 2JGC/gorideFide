package com.goridemoto.repository;

import com.goridemoto.domain.ContactoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoMensajeRepository extends JpaRepository<ContactoMensaje, Long> {
}