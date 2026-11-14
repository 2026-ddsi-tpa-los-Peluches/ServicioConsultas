package ar.edu.utn.dds.k3003.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import ar.edu.utn.dds.k3003.analiticas.DepositoAnalyticsService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class DepositoAnaliticaControllerTest {

    // Bug 4: fechaFin anterior a fechaInicio tiene que responder 400, no 200.
    @Test
    void fechaFinAnteriorAFechaInicioDevuelve400() {
        DepositoAnalyticsService analyticsService =
                org.mockito.Mockito.mock(DepositoAnalyticsService.class);
        DepositoAnaliticaController controller = new DepositoAnaliticaController(analyticsService);

        Instant inicio = Instant.parse("2026-02-01T00:00:00Z");
        Instant fin = Instant.parse("2026-01-01T00:00:00Z");

        ResponseEntity<Object> respuesta = controller.obtenerReporteDeposito("2", inicio, fin);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(analyticsService);
    }
}
