package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.dtos.donaciones.SubCategoriaDTO;
import ar.edu.utn.dds.k3003.service.ServiceSubCategoria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/subcategorias")
public class SubCategoriaController {

    private final ServiceSubCategoria serviceSubCategoria;

    public SubCategoriaController(ServiceSubCategoria serviceSubCategoria) {
        this.serviceSubCategoria = serviceSubCategoria;
    }

    @GetMapping
    public ResponseEntity<List<SubCategoriaDTO>> obtenerSubCategorias() {
        return ResponseEntity.ok(serviceSubCategoria.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSubCategoria(@PathVariable String id) {
        try {
            return ResponseEntity.ok(serviceSubCategoria.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
