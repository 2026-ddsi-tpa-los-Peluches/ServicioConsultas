package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.service.ServiceMision;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/misiones")
public class MisionesController {

    private final ServiceMision serviceMision;

    public MisionesController(ServiceMision serviceMision) {
        this.serviceMision = serviceMision;
    }

    @GetMapping
    public ResponseEntity<?> getMisiones() {
        List<MisionDTO> misiones = serviceMision.obtenerTodas();
        return ResponseEntity.ok(misiones == null ? List.of() : misiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MisionDTO> getMision(@PathVariable String id) {
        try {
            MisionDTO mision = serviceMision.buscarPorId(id);
            return ResponseEntity.ok(mision);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}