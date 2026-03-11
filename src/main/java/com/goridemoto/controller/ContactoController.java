package com.goridemoto.controller;

import com.goridemoto.domain.ContactoMensaje;
import com.goridemoto.service.ContactoMensajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactoController {

    private final ContactoMensajeService contactoMensajeService;

    public ContactoController(ContactoMensajeService contactoMensajeService) {
        this.contactoMensajeService = contactoMensajeService;
    }

    @GetMapping
    public String verContacto(Model model) {
        model.addAttribute("contactoMensaje", new ContactoMensaje());
        return "contact";
    }

    @PostMapping
    public String guardarMensaje(@ModelAttribute("contactoMensaje") ContactoMensaje contactoMensaje,
                                 RedirectAttributes redirectAttributes) {
        contactoMensajeService.guardar(contactoMensaje);
        redirectAttributes.addFlashAttribute("mensajeExito", "Your message has been sent successfully.");
        return "redirect:/contact";
    }
}