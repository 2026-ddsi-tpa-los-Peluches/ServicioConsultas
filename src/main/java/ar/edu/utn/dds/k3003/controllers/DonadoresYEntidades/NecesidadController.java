package ar.edu.utn.dds.k3003.controllers.DonadoresYEntidades;

import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.service.ServiceNecesidad;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/necesidades")
public class NecesidadController {

  private final ServiceNecesidad servicioNecesidad;

  public NecesidadController(ServiceNecesidad servicioNecesidad) {
    this.servicioNecesidad = servicioNecesidad;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{productoID}")
  public ResponseEntity<List<NecesidadMaterialDTO>> getNecesidades(@PathVariable String productoID) {
    try {
      List<NecesidadMaterialDTO> necesidades = this.servicioNecesidad.obtenerNecesidadesInsatisfechasDe(productoID);
      return ResponseEntity.status(HttpStatus.OK).body(necesidades);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/detalle/{id}")
  public ResponseEntity<Object> buscarNecesidadPorId(@PathVariable Integer id) {
    try {
      NecesidadMaterialDTO necesidad = this.servicioNecesidad.buscarPorId(id);
      return ResponseEntity.status(HttpStatus.OK).body(necesidad);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}