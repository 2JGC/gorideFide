package com.goridemoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LocaleController {

    @GetMapping("/cambiar-idioma")
    public String cambiarIdioma(@RequestParam("lang") String lang,
                                @RequestParam(value = "redirect", defaultValue = "/") String redirect,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // Guardar idioma en sesión
        request.getSession().setAttribute("lang", lang);
        return "redirect:" + redirect;
    }
}
