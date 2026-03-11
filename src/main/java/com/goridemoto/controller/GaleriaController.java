package com.goridemoto.controller;

import com.goridemoto.domain.PublicacionGaleria;
import com.goridemoto.service.PublicacionGaleriaService;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/gallery")
public class GaleriaController {

    private final PublicacionGaleriaService publicacionGaleriaService;

    public GaleriaController(PublicacionGaleriaService publicacionGaleriaService) {
        this.publicacionGaleriaService = publicacionGaleriaService;
    }

    @GetMapping
    public String verGaleria(Model model) {
        model.addAttribute("publicaciones", publicacionGaleriaService.listar());
        model.addAttribute("publicacion", new PublicacionGaleria());
        return "gallery";
    }

    @PostMapping
    public String guardarPublicacion(
            @RequestParam("nombreAutor") String nombreAutor,
            @RequestParam("comentario") String comentario,
            @RequestParam("calificacion") Integer calificacion,
            @RequestParam(value = "archivoImagen", required = false) MultipartFile archivoImagen) {

        PublicacionGaleria publicacion = new PublicacionGaleria();
        publicacion.setNombreAutor(nombreAutor);
        publicacion.setComentario(comentario);
        publicacion.setCalificacion(calificacion);

        String rutaImagen = "/img/default-gallery.jpg";

        if (archivoImagen != null && !archivoImagen.isEmpty()) {
            try {
                String carpetaUploads = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File directorio = new File(carpetaUploads);

                if (!directorio.exists()) {
                    directorio.mkdirs();
                }

                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivoImagen.getOriginalFilename();
                File destino = new File(carpetaUploads + nombreArchivo);
                archivoImagen.transferTo(destino);

                rutaImagen = "/uploads/" + nombreArchivo;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        publicacion.setImagen(rutaImagen);
        publicacionGaleriaService.guardar(publicacion);

        return "redirect:/gallery";
    }
}