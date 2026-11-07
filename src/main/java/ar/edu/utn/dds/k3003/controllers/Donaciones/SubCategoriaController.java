package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.documentos.SubCategoriaDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.SubCategoriaMongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/subcategoria")
public class SubCategoriaController {

    private final SubCategoriaMongoRepository subCategoriaMongoRepository;

    public SubCategoriaController(SubCategoriaMongoRepository subCategoriaMongoRepository) {
        this.subCategoriaMongoRepository = subCategoriaMongoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerSubCategoria(@PathVariable String id) {
        Optional<SubCategoriaDoc> subCategoria = subCategoriaMongoRepository.findById(id);
        if (subCategoria.isEmpty()) {
            return ResponseEntity.status(404).body("Subcategoría no encontrada: " + id);
        }
        return ResponseEntity.ok(subCategoria.get());
    }
}