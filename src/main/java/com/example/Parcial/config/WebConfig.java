package com.example.Parcial.config;

import com.example.Parcial.service.ProductoImageStorage;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProductoImageStorage imageStorage;

    public WebConfig(ProductoImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = imageStorage.getUploadDirectory().toUri().toString();
        if (!uploadLocation.endsWith("/")) {
            uploadLocation += "/";
        }
        registry.addResourceHandler("/uploads/productos/**")
                .addResourceLocations(uploadLocation);
    }
}
