package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.exceptions.donaciones.CategoriaNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private Fachada fachada;

    public CategoriaController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> agregarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            CategoriaDTO categoria = this.fachada.agregarCategoria(categoriaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() {
        List<CategoriaDTO> productos = this.fachada.obtenerTodasLasCategorias();
        return ResponseEntity.ok(productos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaDTO> eliminarCategoria(@PathVariable String id) {
        try {
            this.fachada.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (CategoriaNoEncontradaException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerCategoria(@PathVariable String id) {
        try {
            CategoriaDTO categoria = this.fachada.buscarCategoriaPorID(id);
            return ResponseEntity.ok(categoria);
        } catch (CategoriaNoEncontradaException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud de la categoría.");
        }
    }
}
