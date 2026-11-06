package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.analiticas.NecesidadAnalyticsService;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analitica/necesidades")
public class NecesidadAnaliticaController {

    private final NecesidadAnalyticsService analyticsService;

    public NecesidadAnaliticaController(NecesidadAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/insatisfechas")
    public ResponseEntity<Object> obtenerReporteNecesidades(
            @RequestParam(required = false) Integer urgenciaMinima,
            @RequestParam(required = false) String tipoNecesidad,
            @RequestParam(required = false) String categoriaProducto,
            @RequestParam(required = false) String subcategoriaProducto
    ) {
        try {
            List<Document> reporte = this.analyticsService.obtenerReporteNecesidadesInsatisfechas(
                    urgenciaMinima,
                    tipoNecesidad,
                    categoriaProducto,
                    subcategoriaProducto
            );

            return ResponseEntity.status(HttpStatus.OK).body(reporte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la consulta analítica de necesidades: " + e.getMessage());
        }
    }
}