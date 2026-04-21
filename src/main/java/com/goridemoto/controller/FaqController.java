package com.goridemoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqController {

    @GetMapping({"/faq", "/faqs"})
    public String faq() {
        return "redirect:/contact";
    }
}
