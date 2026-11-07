package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.service.ServiceInsignia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/insigniasDonador/{donadorID}")
public class InsigniaDonadorIDController {

    private final ServiceInsignia serviceInsignia;

    public InsigniaDonadorIDController(ServiceInsignia serviceInsignia) {
        this.serviceInsignia = serviceInsignia;
    }

    @GetMapping
    public ResponseEntity<List<InsigniaDTO>> getAllInsigniasDonador(@PathVariable String donadorID) {
        try {
            List<InsigniaDTO> insignias = serviceInsignia.obtenerPorDonador(donadorID);
            return ResponseEntity.ok(insignias);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}