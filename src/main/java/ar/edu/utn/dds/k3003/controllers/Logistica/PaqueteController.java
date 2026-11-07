package ar.edu.utn.dds.k3003.controllers.Logistica;

import ar.edu.utn.dds.k3003.dtos.logistica.PaqueteDTO;
import ar.edu.utn.dds.k3003.service.ServicePaquete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/paquetes")
public class PaqueteController {

    private final ServicePaquete servicePaquete;

    public PaqueteController(ServicePaquete servicePaquete) {
        this.servicePaquete = servicePaquete;
    }

    @GetMapping
    public ResponseEntity<List<PaqueteDTO>> buscarTodos() {
        try {
            List<PaqueteDTO> paquetes = servicePaquete.obtenerTodos();
            return ResponseEntity.ok(paquetes);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPaquetePorId(@PathVariable String id) {
        try {
            PaqueteDTO paqueteDTO = servicePaquete.buscarPorId(id);
            return ResponseEntity.ok(paqueteDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}