package com.goridemoto.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "contacto_mensaje")
public class ContactoMensaje {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String asunto;
    @Column(length = 2000)
    private String mensaje;
    @CreationTimestamp @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    public ContactoMensaje() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}
