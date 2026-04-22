package com.goridemoto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "motocicleta_id")
    private Motocicleta motocicleta;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaFin;

    private Integer dias;
    private BigDecimal total;
    private String nombreCliente;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String provincia;
    private String codigoPostal;
    private Integer numeroPersonas;
    private String notas;
    private Boolean aceptaTerminos;
    private String estado;

    // Email del usuario autenticado que creó la reserva (para historial personal)
    private String emailUsuario;

    @CreationTimestamp @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Reserva() {}
    public Long getId() { return id; }
    public Motocicleta getMotocicleta() { return motocicleta; }
    public void setMotocicleta(Motocicleta motocicleta) { this.motocicleta = motocicleta; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Integer getDias() { return dias; }
    public void setDias(Integer dias) { this.dias = dias; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public Integer getNumeroPersonas() { return numeroPersonas; }
    public void setNumeroPersonas(Integer numeroPersonas) { this.numeroPersonas = numeroPersonas; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Boolean getAceptaTerminos() { return aceptaTerminos; }
    public void setAceptaTerminos(Boolean aceptaTerminos) { this.aceptaTerminos = aceptaTerminos; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
