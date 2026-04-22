package com.goridemoto.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageService {

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.credentials.file}")
    private String credentialsFile;

    private Storage storage;

    private Storage getStorage() throws IOException {
        if (storage == null) {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream(credentialsFile);

            if (serviceAccount == null) {
                throw new IOException(
                    "No se encontró el archivo de credenciales Firebase: " + credentialsFile
                );
            }

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(serviceAccount)
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        }
        return storage;
    }

    /**
     * Sube una imagen a Firebase Storage y retorna la URL pública correcta
     * usando el formato de Firebase Storage REST API (?alt=media).
     */
    public String subirImagen(MultipartFile archivo, String carpeta) throws IOException {
        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreArchivo = carpeta + "/" + UUID.randomUUID().toString() + extension;

        BlobId blobId = BlobId.of(bucketName, nombreArchivo);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(archivo.getContentType())
                .build();

        getStorage().create(blobInfo, archivo.getBytes());

        // URL pública correcta para Firebase Storage
        // Formato: https://firebasestorage.googleapis.com/v0/b/BUCKET/o/RUTA_ENCODED?alt=media
        String rutaEncoded = URLEncoder.encode(nombreArchivo, StandardCharsets.UTF_8.toString());
        return "https://firebasestorage.googleapis.com/v0/b/"
                + bucketName + "/o/"
                + rutaEncoded + "?alt=media";
    }

    /**
     * Elimina una imagen de Firebase Storage dado su URL pública.
     */
    public void eliminarImagen(String urlPublica) {
        if (urlPublica == null || !urlPublica.contains("firebasestorage.googleapis.com")) {
            return;
        }
        try {
            // Extraer el path del archivo desde la URL de Firebase
            // URL formato: .../o/carpeta%2Farchivo.jpg?alt=media
            String path = urlPublica;
            int oIndex = path.indexOf("/o/");
            int altIndex = path.indexOf("?alt=media");
            if (oIndex == -1 || altIndex == -1) return;

            String rutaEncoded = path.substring(oIndex + 3, altIndex);
            String rutaDecoded = java.net.URLDecoder.decode(rutaEncoded, StandardCharsets.UTF_8.toString());

            BlobId blobId = BlobId.of(bucketName, rutaDecoded);
            getStorage().delete(blobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtenerExtension(String nombreOriginal) {
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            return nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }
        return "";
    }
}
