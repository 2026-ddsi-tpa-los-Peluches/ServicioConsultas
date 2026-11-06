package ar.edu.utn.dds.k3003.controllers.Incentivos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint liviano para el keep-alive (Cron.mantenerServicioDespierto). No toca la base
// a propósito, para no generarle carga extra a Postgres solo por mantener el servicio despierto.
@RestController
public class PingController {

  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("pong");
  }
}
