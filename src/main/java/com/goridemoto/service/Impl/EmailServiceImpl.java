package com.goridemoto.service.Impl;

import com.goridemoto.domain.Motocicleta;
import com.goridemoto.domain.Reserva;
import com.goridemoto.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarRecuperacionPassword(String destinatario, String nombre, String resetUrl) {
        String saludo = nombre == null || nombre.trim().isEmpty() ? "Hola" : "Hola " + nombre.trim();
        String mensaje = saludo + ",\n\n"
                + "Recibimos una solicitud para cambiar la contrasena de tu cuenta en Go Ride Moto.\n"
                + "Para crear una nueva contrasena, entra a este enlace:\n\n"
                + resetUrl + "\n\n"
                + "El enlace vence en 30 minutos. Si no solicitaste este cambio, puedes ignorar este correo.\n\n"
                + "Go Ride Moto";

        enviar(destinatario, "Recuperacion de contrasena - Go Ride Moto", mensaje);
    }

    @Override
    public void enviarAgradecimientoReserva(Reserva reserva) {
        if (reserva == null || estaVacio(reserva.getEmail())) {
            return;
        }

        Motocicleta moto = reserva.getMotocicleta();
        String nombreMoto = moto != null ? moto.getNombre() : "Motocicleta seleccionada";
        String nombreCliente = estaVacio(reserva.getNombreCliente()) ? "rider" : reserva.getNombreCliente().trim();

        String mensaje = "Hola " + nombreCliente + ",\n\n"
                + "Gracias por reservar con Go Ride Moto. Estamos emocionados de acompanarte en tu proxima aventura.\n\n"
                + "Resumen de tu reservacion:\n"
                + "- Motocicleta: " + nombreMoto + "\n"
                + "- Fecha de inicio: " + reserva.getFechaInicio() + "\n"
                + "- Fecha de fin: " + reserva.getFechaFin() + "\n"
                + "- Dias: " + reserva.getDias() + "\n"
                + "- Personas: " + valor(reserva.getNumeroPersonas()) + "\n"
                + "- Total: $" + reserva.getTotal() + "\n"
                + "- Estado: " + reserva.getEstado() + "\n\n"
                + "Pronto nos pondremos en contacto contigo para coordinar los detalles finales.\n\n"
                + "Go Ride Moto";

        enviar(reserva.getEmail(), "Gracias por tu reservacion - Go Ride Moto", mensaje);
    }

    private void enviar(String destinatario, String asunto, String mensaje) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(remitente);
        mail.setTo(destinatario);
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mailSender.send(mail);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String valor(Object valor) {
        return valor == null ? "No indicado" : valor.toString();
    }
}
