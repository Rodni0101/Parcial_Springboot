package com.example.Parcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Parcial.model.Producto;


// Para recordar
// Tienes que escribir primerp extends JpaRepository y el import se pone solo, despues importar la clase que este dentro de model
public interface ProductoRepository extends JpaRepository<Producto,Integer>{

}
