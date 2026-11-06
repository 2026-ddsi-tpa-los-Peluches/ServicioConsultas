package ar.edu.utn.dds.k3003.controllers.DonadoresYEntidades;

import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.service.ServiceEntidad;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/entidades")
public class EntidadBeneficaController {

  private final ServiceEntidad servicioEntidad;

  public EntidadBeneficaController(ServiceEntidad servicioEntidad) {
    this.servicioEntidad = servicioEntidad;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntidadBeneficaDTO> getEntidadBenefica(@PathVariable Integer id) {
    try {
      EntidadBeneficaDTO entidadBuscada = servicioEntidad.buscarPorId(id);
      return ResponseEntity.status(HttpStatus.OK).body(entidadBuscada);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping
  public ResponseEntity<List<EntidadBeneficaDTO>> getEntidadBeneficas() {
    List<EntidadBeneficaDTO> entidades = servicioEntidad.obtenerTodas();
    return ResponseEntity.status(HttpStatus.OK).body(entidades);
  }
}