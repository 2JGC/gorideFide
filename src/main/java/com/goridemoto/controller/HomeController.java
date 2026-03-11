package com.goridemoto.controller;

import com.goridemoto.service.MotocicletaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MotocicletaService motocicletaService;

    public HomeController(MotocicletaService motocicletaService) {
        this.motocicletaService = motocicletaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("motos", motocicletaService.listarDisponibles());
        return "index";
    }
}