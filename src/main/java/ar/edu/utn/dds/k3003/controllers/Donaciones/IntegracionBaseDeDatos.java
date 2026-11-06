package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.repositories.donaciones.categoria.CategoriaRepositoryJPA;
import ar.edu.utn.dds.k3003.repositories.donaciones.donacion.DonacionesRepositoryJPA;
import ar.edu.utn.dds.k3003.repositories.donaciones.identificador.IdentificadoresRepositoryJPA;
import ar.edu.utn.dds.k3003.repositories.donaciones.producto.ProductoRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntegracionBaseDeDatos {


    @Autowired
    private DonacionesRepositoryJPA donacionesRepository;
    @Autowired
    private IdentificadoresRepositoryJPA identificadoresRepository;
    @Autowired
    private ProductoRepositoryJPA productoRepository;
    @Autowired
    private CategoriaRepositoryJPA categoriaRepository;



    @PostMapping("/reset")
    public ResponseEntity<String> resetDatabase() {
        donacionesRepository.deleteAll();
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        identificadoresRepository.deleteAll();
        return ResponseEntity.ok("Base de datos reseteada correctamente");
    }
}