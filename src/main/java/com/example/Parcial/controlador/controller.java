package com.example.Parcial.controlador;

import com.example.Parcial.model.Producto;
import com.example.Parcial.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


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
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/";
    }

    private final ProductoRepository productoRepository;

    public controller(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
}
