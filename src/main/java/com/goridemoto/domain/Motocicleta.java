package com.goridemoto.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "motocicleta")
public class Motocicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private BigDecimal precioPorDia;
    private String imagen;
    private Boolean disponible;

    public Motocicleta() {
    }

    public Motocicleta(String nombre, String descripcion, BigDecimal precioPorDia, String imagen, Boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPorDia = precioPorDia;
        this.imagen = imagen;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}