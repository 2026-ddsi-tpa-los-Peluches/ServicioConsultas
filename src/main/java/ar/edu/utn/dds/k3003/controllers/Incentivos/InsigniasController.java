package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.service.ServiceInsignia;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/insignias")
public class InsigniasController {

    private final ServiceInsignia serviceInsignia;

    public InsigniasController(ServiceInsignia serviceInsignia) {
        this.serviceInsignia = serviceInsignia;
    }

    @GetMapping
    public ResponseEntity<?> getAllInsignias() {
        List<InsigniaDTO> insignias = serviceInsignia.obtenerTodas();
        return ResponseEntity.ok(insignias == null ? List.of() : insignias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsigniaDTO> getInsigniaID(@PathVariable String id) {
        try {
            InsigniaDTO insignia = serviceInsignia.buscarPorId(id);
            return ResponseEntity.ok(insignia);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}