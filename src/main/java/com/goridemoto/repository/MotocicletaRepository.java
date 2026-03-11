
package com.goridemoto.repository;

import com.goridemoto.domain.Motocicleta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotocicletaRepository extends JpaRepository<Motocicleta, Long> {
    List<Motocicleta> findByDisponibleTrue();
}