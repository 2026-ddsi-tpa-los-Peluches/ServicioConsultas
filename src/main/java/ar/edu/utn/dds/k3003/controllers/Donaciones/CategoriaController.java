package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.documentos.CategoriaDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.CategoriaMongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaMongoRepository categoriaMongoRepository;

    public CategoriaController(CategoriaMongoRepository categoriaMongoRepository) {
        this.categoriaMongoRepository = categoriaMongoRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDoc>> obtenerCategorias() {
        List<CategoriaDoc> categorias = categoriaMongoRepository.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerCategoria(@PathVariable String id) {
        Optional<CategoriaDoc> categoria = categoriaMongoRepository.findById(id);
        if (categoria.isEmpty()) {
            return ResponseEntity.status(404).body("Categoría no encontrada: " + id);
        }
        return ResponseEntity.ok(categoria.get());
    }
}