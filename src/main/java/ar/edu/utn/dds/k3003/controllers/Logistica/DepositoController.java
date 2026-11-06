package ar.edu.utn.dds.k3003.controllers.Logistica;

import ar.edu.utn.dds.k3003.dtos.logistica.DepositoDTO;
import ar.edu.utn.dds.k3003.service.ServiceDeposito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    private final ServiceDeposito servicioDeposito;

    public DepositoController(ServiceDeposito servicioDeposito) {
        this.servicioDeposito = servicioDeposito;
    }

    @GetMapping
    public ResponseEntity<List<DepositoDTO>> getAllDepositos() {
        List<DepositoDTO> depositos = servicioDeposito.obtenerTodos();
        return ResponseEntity.ok(depositos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepositoById(@PathVariable String id) {
        try {
            DepositoDTO deposito = servicioDeposito.buscarPorId(id);
            return ResponseEntity.ok(deposito);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}