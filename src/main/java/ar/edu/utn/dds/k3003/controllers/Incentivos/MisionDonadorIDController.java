package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades.DonadorMongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/misionesDonador/{donadorID}")
public class MisionDonadorIDController {

    private final DonadorMongoRepository donadorDocRepository;

    public MisionDonadorIDController(DonadorMongoRepository donadorDocRepository) {
        this.donadorDocRepository = donadorDocRepository;
    }

    @GetMapping
    public ResponseEntity<?> getMisionEnCurso(@PathVariable String donadorID) {
        try {
            DonadorDoc donador = donadorDocRepository.findByDonadorId(donadorID).orElse(null);
            if (donador == null || donador.getMisionActual() == null) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.ok(donador.getMisionActual());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }
}