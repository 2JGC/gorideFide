package com.goridemoto.domain;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String rol;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiration;

    public Usuario() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public LocalDateTime getResetPasswordTokenExpiration() { return resetPasswordTokenExpiration; }
    public void setResetPasswordTokenExpiration(LocalDateTime resetPasswordTokenExpiration) { this.resetPasswordTokenExpiration = resetPasswordTokenExpiration; }
}
