package com.example.Parcial.controlador;

import com.example.Parcial.model.Producto;
import com.example.Parcial.repository.ProductoRepository;
import com.example.Parcial.service.ProductoImageStorage;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller

public class controller {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "inicio";
    }

    @GetMapping("/Editar/{id}")
    public String Editar_Producto(@PathVariable Integer id, Model model) {
        Producto producto = 
        productoRepository.findById(id).orElseThrow();
        model.addAttribute("producto", producto);
        return "editar";
    }

    @GetMapping("/Crear")
    public String Crear_Producto(Model model) {
        model.addAttribute("producto", new Producto());
        return "crear";
    }

    @PostMapping("/Crear")
    public String guardarProducto(
            @ModelAttribute Producto producto,
            @RequestParam("archivoImagen") MultipartFile imagen,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            producto.setImagen(imageStorage.save(imagen));
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/Crear";
        }
        productoRepository.save(producto);
        return "redirect:/";
    }

    @PostMapping("/Editar/{id}")
    public String actualizarProducto(
            @PathVariable Integer id,
            @ModelAttribute Producto datosFormulario,
            @RequestParam(value = "archivoImagen", required = false) MultipartFile imagen,
            Model model) throws IOException {
        Producto producto = productoRepository.findById(id).orElseThrow();
        producto.setNombre(datosFormulario.getNombre());
        producto.setCategoria(datosFormulario.getCategoria());
        producto.setPrecio(datosFormulario.getPrecio());
        producto.setCantidadDisponible(datosFormulario.getCantidadDisponible());
        producto.setDescripcion(datosFormulario.getDescripcion());

        if (imagen != null && !imagen.isEmpty()) {
            try {
                producto.setImagen(imageStorage.save(imagen));
            } catch (IllegalArgumentException exception) {
                model.addAttribute("producto", producto);
                model.addAttribute("error", exception.getMessage());
                return "editar";
            }
        }

        productoRepository.save(producto);
        return "redirect:/";
    }

    @PostMapping("/Eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) throws IOException {
        Producto producto = productoRepository.findById(id).orElseThrow();
        productoRepository.delete(producto);
        imageStorage.delete(producto.getImagen());
        return "redirect:/";
    }

    private final ProductoRepository productoRepository;
    private final ProductoImageStorage imageStorage;

    public controller(ProductoRepository productoRepository, ProductoImageStorage imageStorage) {
        this.productoRepository = productoRepository;
        this.imageStorage = imageStorage;
    }
}
