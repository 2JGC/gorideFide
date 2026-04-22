package com.goridemoto.controller;

import com.goridemoto.domain.Motocicleta;
import com.goridemoto.domain.Reserva;
import com.goridemoto.service.EmailService;
import com.goridemoto.service.MotocicletaService;
import com.goridemoto.service.ReservaService;
import java.security.Principal;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private final MotocicletaService motocicletaService;
    private final ReservaService reservaService;
    private final EmailService emailService;

    public ReservaController(MotocicletaService motocicletaService,
                             ReservaService reservaService,
                             EmailService emailService) {
        this.motocicletaService = motocicletaService;
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    private String getEmail(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = ((OAuth2AuthenticationToken) principal).getPrincipal();
            return oauthUser.getAttribute("email");
        }
        return principal != null ? principal.getName() : null;
    }

    @GetMapping("/book-now")
    public String mostrarBookNow(Model model) {
        model.addAttribute("motos", motocicletaService.listarDisponibles());
        model.addAttribute("reserva", new Reserva());
        return "book-now";
    }

    @PostMapping("/book-now")
    public String procesarBookNow(@ModelAttribute Reserva reserva,
            @RequestParam("motocicletaId") Long motocicletaId,
            Model model, HttpSession session) {

        Motocicleta moto = motocicletaService.buscarPorId(motocicletaId);

        if (moto == null || reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            model.addAttribute("motos", motocicletaService.listarDisponibles());
            model.addAttribute("error", "Debe seleccionar moto y fechas válidas.");
            return "book-now";
        }

        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (dias <= 0) {
            model.addAttribute("motos", motocicletaService.listarDisponibles());
            model.addAttribute("error", "La fecha final debe ser posterior a la fecha inicial.");
            return "book-now";
        }

        reserva.setMotocicleta(moto);
        reserva.setDias((int) dias);
        reserva.setTotal(moto.getPrecioPorDia().multiply(java.math.BigDecimal.valueOf(dias)));
        session.setAttribute("reservaPendiente", reserva);
        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model, Principal principal) {
        Reserva reserva = (Reserva) session.getAttribute("reservaPendiente");
        if (reserva == null) return "redirect:/book-now";

        // Pre-llenar email si está autenticado (local o OAuth2)
        String email = getEmail(principal);
        if (email != null && (reserva.getEmail() == null || reserva.getEmail().trim().isEmpty())) {
            reserva.setEmail(email);
        }
        model.addAttribute("reserva", reserva);
        return "checkout";
    }

    @PostMapping("/checkout/confirm")
    public String confirmarReserva(@ModelAttribute Reserva reserva,
            @RequestParam("motocicletaId") Long motocicletaId,
            HttpSession session, Principal principal) {

        Motocicleta moto = motocicletaService.buscarPorId(motocicletaId);
        reserva.setMotocicleta(moto);
        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        reserva.setDias((int) dias);
        reserva.setTotal(moto.getPrecioPorDia().multiply(java.math.BigDecimal.valueOf(dias)));
        reserva.setEstado("PENDIENTE");

        // Vincular con el usuario autenticado (local o OAuth2)
        String email = getEmail(principal);
        if (email != null) {
            reserva.setEmailUsuario(email);
        }

        reservaService.guardar(reserva);
        try {
            emailService.enviarAgradecimientoReserva(reserva);
        } catch (Exception ex) {
            logger.warn("No se pudo enviar el correo de agradecimiento de la reserva", ex);
        }
        session.removeAttribute("reservaPendiente");
        session.setAttribute("reservaConfirmada", true);
        return "redirect:/transaction-completed";
    }

    @GetMapping("/transaction-completed")
    public String transaccionCompletada(HttpSession session) {
        Boolean confirmada = (Boolean) session.getAttribute("reservaConfirmada");
        if (!Boolean.TRUE.equals(confirmada)) return "redirect:/";
        session.removeAttribute("reservaConfirmada");
        return "transaction-completed";
    }
}
