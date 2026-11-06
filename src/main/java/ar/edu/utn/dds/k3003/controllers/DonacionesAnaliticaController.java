package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.analiticas.DonacionesAnalyticsService;
import ar.edu.utn.dds.k3003.dtos.ReporteCalidadYDonadoresDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analitica/calidad-donadores")
public class DonacionesAnaliticaController {

    private final DonacionesAnalyticsService analyticsService;

    public DonacionesAnaliticaController(DonacionesAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/reporte-general")
    public ResponseEntity<ReporteCalidadYDonadoresDTO> obtenerReporteGeneral(
            @RequestParam(defaultValue = "10") int limiteDonadores
    ) {
        ReporteCalidadYDonadoresDTO reporte = this.analyticsService.obtenerReporteCompleto(limiteDonadores);
        return ResponseEntity.status(HttpStatus.OK).body(reporte);
    }
}