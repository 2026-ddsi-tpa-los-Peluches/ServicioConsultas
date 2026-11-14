package ar.edu.utn.dds.k3003.analiticas;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.dds.k3003.dtos.DepositoReporteDTO;
import java.time.Instant;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class DepositoAnalyticsServiceTest {

    @Autowired private MongoTemplate mongoTemplate;

    // El contexto (y el Mongo embebido) se comparte entre clases de test: hay que limpiar para que
    // depósitos/asignaciones de otro test no se mezclen con los de este.
    @BeforeEach
    void limpiarColecciones() {
        mongoTemplate.getDb().getCollection("depositos").deleteMany(new Document());
        mongoTemplate.getDb().getCollection("asignaciones").deleteMany(new Document());
    }

    private DepositoAnalyticsService service;

    private DepositoAnalyticsService service() {
        if (service == null) {
            service = new DepositoAnalyticsService(mongoTemplate);
        }
        return service;
    }

    private final Instant inicio = Instant.parse("2020-01-01T00:00:00Z");
    private final Instant fin = Instant.parse("2030-01-01T00:00:00Z");

    // Bug 1: una sola asignación que pasó de ASIGNADA a COMPLETADA tiene que contar 1 COMPLETADA,
    // no {"ASIGNADA":1,"COMPLETADA":1}: el estado anterior se tiene que restar, no solo sumar el nuevo.
    @Test
    void cantidadAsignacionesPorEstadoSoloCuentaElEstadoActual() {
        mongoTemplate.getCollection("depositos").insertOne(new Document("_id", "2")
                .append("nombre", "Depósito Medrano")
                .append("capacidadMaxima", 1500)
                .append("stockTotal", 30));

        mongoTemplate.getCollection("asignaciones").insertOne(new Document("_id", "1")
                .append("depositoId", "2")
                .append("entidadId", "2")
                .append("estado", "COMPLETADA")
                .append("unidades", 40));

        DepositoReporteDTO reporte = service().obtenerReporteDeposito("2", inicio, fin);

        assertThat(reporte.cantidadAsignacionesPorEstado())
                .containsOnly(org.assertj.core.api.Assertions.entry("COMPLETADA", 1L));
        assertThat(reporte.cantidadDeEntidadesDistintasAtendidas()).isEqualTo(1);
    }

    // Bug 3: un depósito sin asignaciones ni stock no puede devolver null en estos campos.
    @Test
    void depositoSinAsignacionesDevuelveValoresPorDefecto() {
        mongoTemplate.getCollection("depositos").insertOne(new Document("_id", "9")
                .append("nombre", "Depósito vacío")
                .append("capacidadMaxima", 100));

        DepositoReporteDTO reporte = service().obtenerReporteDeposito("9", inicio, fin);

        assertThat(reporte.cantidadAsignacionesPorEstado()).isEmpty();
        assertThat(reporte.cantidadDeEntidadesDistintasAtendidas()).isZero();
        assertThat(reporte.stockTotal()).isZero();
    }
}
