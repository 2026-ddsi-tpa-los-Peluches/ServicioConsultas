package ar.edu.utn.dds.k3003.controllers.Donaciones;

import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.service.ServiceDonacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/donaciones")
public class DonacionController {

    private final ServiceDonacion servicioDonacion;

    public DonacionController(ServiceDonacion servicioDonacion) {
        this.servicioDonacion = servicioDonacion;
    }

    @GetMapping
    public ResponseEntity<List<DonacionDTO>> obtenerDonaciones() {
        List<DonacionDTO> donaciones = this.servicioDonacion.obtenerTodas();
        return ResponseEntity.ok(donaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonacionDTO> obtenerDonacion(@PathVariable String id) {
        try {
            DonacionDTO donacionBuscada = this.servicioDonacion.buscarPorId(id);
            return ResponseEntity.ok(donacionBuscada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DonacionDTO>> buscarDonacionPorDonadorYFecha(
            @RequestParam String donadorID,
            @RequestParam String fechaInicio) {
        try {
            LocalDate fecha = LocalDate.parse(fechaInicio);
            List<DonacionDTO> donaciones = this.servicioDonacion.buscarPorDonadorYFecha(donadorID, fecha);
            return ResponseEntity.ok(donaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}