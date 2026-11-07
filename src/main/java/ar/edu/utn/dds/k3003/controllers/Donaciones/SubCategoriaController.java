package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.service.ServiceCategoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/subCategoria")
public class SubCategoriaController {

    private final ServiceCategoria serviceCategoria;

    public SubCategoriaController(ServiceCategoria serviceCategoria) {
        this.serviceCategoria = serviceCategoria;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() {
        return ResponseEntity.ok(serviceCategoria.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoria(@PathVariable String id) {
        try {
            return ResponseEntity.ok(serviceCategoria.buscarPorId(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}