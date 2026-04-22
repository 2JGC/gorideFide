package com.goridemoto.controller;

import com.goridemoto.domain.PublicacionGaleria;
import com.goridemoto.service.FirebaseStorageService;
import com.goridemoto.service.PublicacionGaleriaService;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/gallery")
public class GaleriaController {

    private final PublicacionGaleriaService publicacionGaleriaService;
    private final FirebaseStorageService firebaseStorageService;

    public GaleriaController(PublicacionGaleriaService publicacionGaleriaService,
                              FirebaseStorageService firebaseStorageService) {
        this.publicacionGaleriaService = publicacionGaleriaService;
        this.firebaseStorageService = firebaseStorageService;
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
            @RequestParam(value = "archivoImagen", required = false) MultipartFile archivoImagen,
            RedirectAttributes redirectAttributes) {

        PublicacionGaleria publicacion = new PublicacionGaleria();
        publicacion.setNombreAutor(nombreAutor);
        publicacion.setComentario(comentario);
        publicacion.setCalificacion(calificacion);

        String urlImagen = "/img/default-gallery.jpg";
        if (archivoImagen != null && !archivoImagen.isEmpty()) {
            try {
                urlImagen = firebaseStorageService.subirImagen(archivoImagen, "galeria");
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Error al subir la imagen.");
            }
        }

        publicacion.setImagen(urlImagen);
        publicacionGaleriaService.guardar(publicacion);
        return "redirect:/gallery";
    }
}
