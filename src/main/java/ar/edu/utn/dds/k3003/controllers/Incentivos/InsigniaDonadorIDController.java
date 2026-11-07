package ar.edu.utn.dds.k3003.controllers.Incentivos;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades.DonadorMongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/insigniasDonador/{donadorID}")
public class InsigniaDonadorIDController {

//    private final DonadorMongoRepository donadorDocRepository;
//
//    public InsigniaDonadorIDController(DonadorMongoRepository donadorDocRepository) {
//        this.donadorDocRepository = donadorDocRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<String>> getAllInsigniasDonador(@PathVariable String donadorID) {
//        try {
//            DonadorDoc donador = donadorDocRepository.findByDonadorId(donadorID)
//                    .orElseThrow(() -> new NoSuchElementException("Donador no encontrado: " + donadorID));
//            return ResponseEntity.ok(donador.getInsignias());
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}