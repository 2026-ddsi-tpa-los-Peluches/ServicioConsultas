package ar.edu.utn.dds.k3003.controllers.DonadoresYEntidades;


import ar.edu.utn.dds.k3003.analiticas.DonadorAnalyticsService;
import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.service.ServiceDonador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donadores")
public class DonadorController {

  private final ServiceDonador servicioDonador;

  public DonadorController(ServiceDonador servicioDonador) {
    this.servicioDonador = servicioDonador;
  }

  @GetMapping("/{id}")
  public ResponseEntity<DonadorDTO> getDonador(@PathVariable Integer id) {
    DonadorDTO donadorBuscado = servicioDonador.obtenerPorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(donadorBuscado);
  }

  @GetMapping
  public ResponseEntity<List<DonadorDTO>> getDonadores() {
    List<DonadorDTO> donadores = servicioDonador.obtenerTodos();
    return ResponseEntity.status(HttpStatus.OK).body(donadores);
  }

  @GetMapping("/{id}/puede-donar")
  public ResponseEntity<Map<String, Boolean>> puedeDonar(@PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK)
            .body(Map.of("puedeDonar", this.servicioDonador.puedeDonar(id)));
  }

  @GetMapping("/{id}/quejas")
  public ResponseEntity<List<QuejaDTO>> getQuejas(@PathVariable Integer id) {
    List<QuejaDTO> quejas = this.servicioDonador.obtenerQuejasDe(id);
    return ResponseEntity.status(HttpStatus.OK).body(quejas);
  }

  @GetMapping("/{id}/estadisticas")
  public ResponseEntity<DonadorStatsDTO> getEstadisticas(@PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK).body(this.servicioDonador.estadisticasDonador(id));
  }
  // @GetMapping
  // public ResponseEntity<DonadorDTO> getDonadorByID(@RequestParam String donadorID) {
  //  return ResponseEntity.status(HttpStatus.OK).body(this.fachada.buscarDonadorPorID(donadorID));
  // }
}
