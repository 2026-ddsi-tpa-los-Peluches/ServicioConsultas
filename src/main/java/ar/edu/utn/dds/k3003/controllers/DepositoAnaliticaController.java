package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.analiticas.DepositoAnalyticsService;
import ar.edu.utn.dds.k3003.dtos.DepositoReporteDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/analitica/depositos")
public class DepositoAnaliticaController {

    private final DepositoAnalyticsService analyticsService;

    public DepositoAnaliticaController(DepositoAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{depositoId}")
    public ResponseEntity<Object> obtenerReporteDeposito(
            @PathVariable String depositoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaFin
    ) {
        try {
            // Llamamos al servicio que ejecuta el pipeline de agregación en MongoDB
            DepositoReporteDTO reporte = this.analyticsService.obtenerReporteDeposito(depositoId, fechaInicio, fechaFin);
            
            if (reporte == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró información analítica para el depósito ID: " + depositoId);
            }
            
            return ResponseEntity.status(HttpStatus.OK).body(reporte);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros inválidos en la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la consulta analítica del depósito.");
        }
    }
}