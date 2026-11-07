package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.service.ServiceMision;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/misionesDonador/{donadorID}")
public class MisionDonadorIDController {

    private final ServiceMision serviceMision;

    public MisionDonadorIDController(ServiceMision serviceMision) {
        this.serviceMision = serviceMision;
    }

    @GetMapping
    public ResponseEntity<MisionDTO> getMisionEnCurso(@PathVariable String donadorID) {
        try {
            MisionDTO mision = serviceMision.obtenerEnCursoPorDonador(donadorID);
            return ResponseEntity.ok(mision);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}