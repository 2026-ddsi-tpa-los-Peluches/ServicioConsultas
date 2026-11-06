package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.service.ServiceIdentificador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/identificadores")
public class IdentificadorController {

    private final ServiceIdentificador servicioIdentificador;

    public IdentificadorController(ServiceIdentificador servicioIdentificador) {
        this.servicioIdentificador = servicioIdentificador;
    }

    @GetMapping
    public ResponseEntity<List<IdentificadorDTO>> obtenerIdentificadores() {
        List<IdentificadorDTO> identificadores = this.servicioIdentificador.obtenerTodos();
        return ResponseEntity.ok(identificadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerIdentificadorPorId(@PathVariable String id) {
        try {
            IdentificadorDTO identificador = this.servicioIdentificador.buscarPorId(id);
            return ResponseEntity.ok(identificador);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud del identificador.");
        }
    }
}