package com.example.Parcial.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class controller {
@GetMapping("/")
    public String index(){
    return "inicio";
    }

@GetMapping("/Editar")
    public String Editar_Producto(){
        return "editar";
    }

@GetMapping("/Crear")
    public String Crear_Producto(){
        return "crear";
    }
}
