package com.goridemoto.repository;

import com.goridemoto.domain.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEmailUsuarioOrderByFechaCreacionDesc(String emailUsuario);
}
