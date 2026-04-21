package com.goridemoto.config;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ViewSessionAdvice {
    @ModelAttribute
    public void ensureSession(HttpSession session) {}
}
