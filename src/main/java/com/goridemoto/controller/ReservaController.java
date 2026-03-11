package com.goridemoto.controller;

import com.goridemoto.domain.Motocicleta;
import com.goridemoto.domain.Reserva;
import com.goridemoto.service.MotocicletaService;
import com.goridemoto.service.ReservaService;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservaController {

    private final MotocicletaService motocicletaService;
    private final ReservaService reservaService;

    public ReservaController(MotocicletaService motocicletaService, ReservaService reservaService) {
        this.motocicletaService = motocicletaService;
        this.reservaService = reservaService;
    }

    @GetMapping("/book-now")
    public String mostrarBookNow(Model model) {
        List<Motocicleta> motos = motocicletaService.listarDisponibles();

        model.addAttribute("motos", motos);
        model.addAttribute("reserva", new Reserva());
        return "book-now";
    }

    @PostMapping("/book-now")
    public String procesarBookNow(@ModelAttribute Reserva reserva,
            @RequestParam("motocicletaId") Long motocicletaId,
            Model model) {

        Motocicleta moto = motocicletaService.buscarPorId(motocicletaId);

        if (moto == null || reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            model.addAttribute("motocicletas", motocicletaService.listarDisponibles());
            model.addAttribute("error", "Debe seleccionar moto y fechas válidas.");
            return "book-now";
        }

        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());

        if (dias <= 0) {
            model.addAttribute("motocicletas", motocicletaService.listarDisponibles());
            model.addAttribute("error", "La fecha final debe ser posterior a la fecha inicial.");
            return "book-now";
        }

        reserva.setMotocicleta(moto);
        reserva.setDias((int) dias);
        reserva.setTotal(moto.getPrecioPorDia().multiply(java.math.BigDecimal.valueOf(dias)));

        model.addAttribute("reserva", reserva);
        return "checkout";
    }

    @PostMapping("/checkout/confirm")
    public String confirmarReserva(@ModelAttribute Reserva reserva,
            @RequestParam("motocicletaId") Long motocicletaId) {

        Motocicleta moto = motocicletaService.buscarPorId(motocicletaId);
        reserva.setMotocicleta(moto);

        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        reserva.setDias((int) dias);
        reserva.setTotal(moto.getPrecioPorDia().multiply(java.math.BigDecimal.valueOf(dias)));
        reserva.setEstado("PENDIENTE");

        reservaService.guardar(reserva);

        return "redirect:/transaction-completed";
    }

    @GetMapping("/transaction-completed")
    public String transaccionCompletada() {
        return "transaction-completed";
    }
}
