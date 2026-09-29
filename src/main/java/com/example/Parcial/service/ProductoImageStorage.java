package com.example.Parcial.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoImageStorage {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final long MAX_IMAGE_PIXELS = 40_000_000;

    private final Path uploadDirectory;

    public ProductoImageStorage(@Value("${app.upload.dir:uploads/productos}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public String save(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Selecciona una imagen para el producto.");
        }
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("La imagen no puede superar los 5 MB.");
        }

        String extension = getImageExtension(image);
        Files.createDirectories(uploadDirectory);
        String filename = UUID.randomUUID() + "." + extension;
        image.transferTo(uploadDirectory.resolve(filename));
        return filename;
    }

    public Path getUploadDirectory() {
        return uploadDirectory;
    }

    public void delete(String filename) throws IOException {
        if (filename == null || filename.isBlank()) {
            return;
        }

        Path imagePath = uploadDirectory.resolve(filename).normalize();
        if (!imagePath.getParent().equals(uploadDirectory)) {
            throw new IllegalArgumentException("La ruta de la imagen no es válida.");
        }
        Files.deleteIfExists(imagePath);
    }

    private String getImageExtension(MultipartFile image) throws IOException {
        try (InputStream input = image.getInputStream();
             ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {
            if (imageInput == null) {
                throw new IllegalArgumentException("El archivo seleccionado no es una imagen válida.");
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Usa una imagen JPG, PNG o GIF válida.");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                String format = reader.getFormatName().toLowerCase(Locale.ROOT);
                long pixels = (long) reader.getWidth(0) * reader.getHeight(0);
                if (pixels > MAX_IMAGE_PIXELS) {
                    throw new IllegalArgumentException("La resolución de la imagen es demasiado grande.");
                }
                return switch (format) {
                    case "jpeg" -> "jpg";
                    case "png", "gif" -> format;
                    default -> throw new IllegalArgumentException("Usa una imagen JPG, PNG o GIF.");
                };
            } finally {
                reader.dispose();
            }
        }
    }
}
