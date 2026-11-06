package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.analiticas.DonadorAnalyticsService;
import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analitica/donadores")
public class DonadorAnaliticaController {

    private final DonadorAnalyticsService analyticsService;

    public DonadorAnaliticaController(DonadorAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{donadorId}")
    public ResponseEntity<Object> obtenerReporteDonador(@PathVariable String donadorId) {
        try {
            DonadorDoc reporte = this.analyticsService.obtenerReporteDonador(donadorId);

            if (reporte == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró información analítica para el donador ID: " + donadorId);
            }

            return ResponseEntity.status(HttpStatus.OK).body(reporte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la consulta analítica del donador: " + e.getMessage());
        }
    }
}