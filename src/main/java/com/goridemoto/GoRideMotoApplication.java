package com.goridemoto;

import com.goridemoto.domain.Motocicleta;
import com.goridemoto.domain.PublicacionGaleria;
import com.goridemoto.repository.MotocicletaRepository;
import com.goridemoto.repository.PublicacionGaleriaRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GoRideMotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoRideMotoApplication.class, args);
    }

    @Bean
    public CommandLineRunner cargarDatos(MotocicletaRepository motoRepo,
                                         PublicacionGaleriaRepository galeriaRepo) {
        return args -> {
            if (motoRepo.count() == 0) {
                motoRepo.save(new Motocicleta(
                        "Honda CRF300 Rally",
                        "Moto versátil lista para aventura en Costa Rica.",
                        new BigDecimal("100"),
                        "/img/crf300.jpg",
                        true
                ));

                motoRepo.save(new Motocicleta(
                        "Kawasaki KLR650",
                        "Moto cómoda y confiable para rutas largas.",
                        new BigDecimal("85"),
                        "/img/klr650.jpg",
                        true
                ));

                motoRepo.save(new Motocicleta(
                        "Honda CRF250L",
                        "Excelente opción para aventura y ciudad.",
                        new BigDecimal("75"),
                        "/img/crf250l.jpg",
                        true
                ));
            }

            if (galeriaRepo.count() == 0) {
                galeriaRepo.save(new PublicacionGaleria(
                        "Marco Rodriguez",
                        "Unforgettable ride through the Nicoya Peninsula.",
                        5,
                        "/img/default-gallery.jpg"
                ));

                galeriaRepo.save(new PublicacionGaleria(
                        "Sarah Jenkins",
                        "Best rental experience I have had.",
                        5,
                        "/img/default-gallery.jpg"
                ));
            }
        };
    }
}