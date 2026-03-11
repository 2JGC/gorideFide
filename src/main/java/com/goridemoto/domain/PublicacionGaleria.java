package com.goridemoto.domain;

import javax.persistence.*;

@Entity
@Table(name = "publicacion_galeria")
public class PublicacionGaleria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreAutor;

    @Column(length = 1000)
    private String comentario;

    private Integer calificacion;

    @Column(length = 500)
    private String imagen;

    public PublicacionGaleria() {
    }

    public PublicacionGaleria(String nombreAutor, String comentario, Integer calificacion, String imagen) {
        this.nombreAutor = nombreAutor;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}