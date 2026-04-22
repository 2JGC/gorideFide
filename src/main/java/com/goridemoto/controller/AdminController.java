package com.goridemoto.controller;

import com.goridemoto.domain.ContactoMensaje;
import com.goridemoto.domain.Motocicleta;
import com.goridemoto.domain.Reserva;
import com.goridemoto.service.ContactoMensajeService;
import com.goridemoto.service.FirebaseStorageService;
import com.goridemoto.service.MotocicletaService;
import com.goridemoto.service.ReservaService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ContactoMensajeService contactoMensajeService;
    private final ReservaService reservaService;
    private final MotocicletaService motocicletaService;
    private final FirebaseStorageService firebaseStorageService;

    public AdminController(ContactoMensajeService contactoMensajeService,
            ReservaService reservaService, MotocicletaService motocicletaService,
            FirebaseStorageService firebaseStorageService) {
        this.contactoMensajeService = contactoMensajeService;
        this.reservaService = reservaService;
        this.motocicletaService = motocicletaService;
        this.firebaseStorageService = firebaseStorageService;
    }

    @GetMapping
    public String dashboard(Model model) {
        List<ContactoMensaje> mensajes = contactoMensajeService.listar();
        List<Reserva> reservas = reservaService.listar();
        List<Motocicleta> motocicletas = motocicletaService.listarTodas();
        long totalReservas = reservas.size();
        long pendientes = reservas.stream().filter(r -> "PENDIENTE".equalsIgnoreCase(safe(r.getEstado()))).count();
        long confirmadas = reservas.stream().filter(r -> "CONFIRMADA".equalsIgnoreCase(safe(r.getEstado()))).count();
        long canceladas = reservas.stream().filter(r -> "CANCELADA".equalsIgnoreCase(safe(r.getEstado()))).count();
        model.addAttribute("totalMensajes", mensajes.size());
        model.addAttribute("totalReservas", totalReservas);
        model.addAttribute("totalMotos", motocicletas.size());
        model.addAttribute("porcentajePendientes", pct(pendientes, totalReservas));
        model.addAttribute("porcentajeConfirmadas", pct(confirmadas, totalReservas));
        model.addAttribute("porcentajeCanceladas", pct(canceladas, totalReservas));
        model.addAttribute("reservasRecientes", reservas);
        return "admin/dashboard";
    }

    @GetMapping("/contactos")
    public String contactos(Model model) {
        model.addAttribute("mensajes", contactoMensajeService.listar());
        return "admin/contactos";
    }

    @GetMapping("/reservas")
    public String reservas(Model model) {
        model.addAttribute("reservas", reservaService.listar());
        return "admin/reservas";
    }

    @PostMapping("/reservas/{id}/estado")
    public String actualizarEstadoReserva(@PathVariable Long id,
            @RequestParam("estado") String estado, RedirectAttributes ra) {
        if (!estado.equals("PENDIENTE") && !estado.equals("CONFIRMADA") && !estado.equals("CANCELADA")) {
            ra.addFlashAttribute("adminError", "Estado inválido.");
            return "redirect:/admin/reservas";
        }
        reservaService.actualizarEstado(id, estado);
        ra.addFlashAttribute("adminExito", "Reservation status updated.");
        return "redirect:/admin/reservas";
    }

    @GetMapping("/motocicletas")
    public String motocicletas(Model model) {
        model.addAttribute("motocicletas", motocicletaService.listarTodas());
        return "admin/motocicletas";
    }

    @PostMapping("/motocicletas/{id}")
    public String actualizarMotocicleta(@PathVariable Long id,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "disponible", defaultValue = "false") boolean disponible,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            RedirectAttributes ra) {

        Motocicleta moto = motocicletaService.buscarPorId(id);
        if (moto != null) {
            moto.setDescripcion(descripcion);
            moto.setDisponible(disponible);
            if (imagen != null && !imagen.isEmpty()) {
                try {
                    firebaseStorageService.eliminarImagen(moto.getImagen());
                    moto.setImagen(firebaseStorageService.subirImagen(imagen, "motos"));
                } catch (IOException e) {
                    e.printStackTrace();
                    ra.addFlashAttribute("adminError", "Error al subir la imagen.");
                    return "redirect:/admin/motocicletas";
                }
            }
            motocicletaService.guardar(moto);
        }
        ra.addFlashAttribute("adminExito", "Motorcycle updated successfully.");
        return "redirect:/admin/motocicletas";
    }

    private long pct(long parte, long total) {
        return total == 0 ? 0 : Math.round((parte * 100.0) / total);
    }
    private String safe(String v) { return v == null ? "" : v; }
}
