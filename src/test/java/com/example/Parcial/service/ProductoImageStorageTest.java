package com.example.Parcial.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class ProductoImageStorageTest {

    @TempDir
    Path uploadDirectory;

    @Test
    void savesValidatedImageWithGeneratedName() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), "png", output);
        byte[] imageBytes = output.toByteArray();
        ProductoImageStorage storage = new ProductoImageStorage(uploadDirectory.toString());

        String filename = storage.save(new MockMultipartFile(
                "imagen", "untrusted-name.exe", "application/octet-stream", imageBytes));

        assertTrue(filename.endsWith(".png"));
        assertArrayEquals(imageBytes, Files.readAllBytes(uploadDirectory.resolve(filename)));
    }

    @Test
    void rejectsFilesThatAreNotImages() {
        ProductoImageStorage storage = new ProductoImageStorage(uploadDirectory.toString());
        MockMultipartFile notAnImage = new MockMultipartFile(
                "imagen", "foto.png", "image/png", "esto no es una imagen".getBytes());

        assertThrows(IllegalArgumentException.class, () -> storage.save(notAnImage));
    }
}
