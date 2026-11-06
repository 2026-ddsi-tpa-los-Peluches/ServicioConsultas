package ar.edu.utn.dds.k3003.controllers.Logistica;

import ar.edu.utn.dds.k3003.dtos.logistica.AsignacionDTO;
import ar.edu.utn.dds.k3003.service.ServiceAsignacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionController {

    private final ServiceAsignacion servicioAsignacion;

    public AsignacionController(ServiceAsignacion servicioAsignacion) {
        this.servicioAsignacion = servicioAsignacion;
    }

    @GetMapping
    public ResponseEntity<List<AsignacionDTO>> getAllAsignaciones() {
        try {
            List<AsignacionDTO> asignaciones = servicioAsignacion.obtenerTodas();
            return ResponseEntity.ok(asignaciones);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAsignacion(@PathVariable String id) {
        try {
            AsignacionDTO asignacion = servicioAsignacion.buscarPorId(id);
            return ResponseEntity.ok(asignacion);
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}