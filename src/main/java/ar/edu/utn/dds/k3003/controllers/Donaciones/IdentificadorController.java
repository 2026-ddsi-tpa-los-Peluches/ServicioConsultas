package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.exceptions.donaciones.IdentificadorNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/identificadores")
public class IdentificadorController {

    private Fachada fachada;

    public IdentificadorController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<IdentificadorDTO> agregarIdentificador(@RequestBody IdentificadorDTO identificadorDTO) {
        try {
            IdentificadorDTO categoria = this.fachada.agregarIdentificador(identificadorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<IdentificadorDTO>> obtenerCategorias() {
        List<IdentificadorDTO> productos = this.fachada.obtenerTodasLosIdentificadores();
        return ResponseEntity.ok(productos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IdentificadorDTO> eliminarIdentificador(@PathVariable String id) {
        try {
            this.fachada.eliminarIdentificador(id);
            return ResponseEntity.noContent().build();
        } catch (IdentificadorNoEncontradoException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
