package ar.edu.utn.dds.k3003.analiticas;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class DonadorAnalyticsServiceTest {

    @Autowired private MongoTemplate mongoTemplate;

    // El contexto (y el Mongo embebido) se comparte entre clases de test: hay que limpiar para que
    // donadores/donaciones de otro test no se mezclen con los de este.
    @BeforeEach
    void limpiarColecciones() {
        mongoTemplate.getDb().getCollection("donadores").deleteMany(new Document());
        mongoTemplate.getDb().getCollection("donaciones").deleteMany(new Document());
    }

    private DonadorAnalyticsService service() {
        return new DonadorAnalyticsService(mongoTemplate);
    }

    // Bug 8: cantidadesPorEstado tiene que reflejar el estado ACTUAL de cada donación, no la cantidad
    // de veces que el donador "pasó" por cada estado (eso es lo que hacía el contador de solo sumar).
    @Test
    void cantidadesPorEstadoReflejaElEstadoActualDeCadaDonacion() {
        mongoTemplate.getCollection("donadores").insertOne(new Document("_id", "2")
                .append("nombre", "Martín")
                .append("apellido", "Gómez")
                .append("categoriaActual", "COLABORADOR")
                // Contador viejo, roto, con las 4 donaciones sumadas en INGRESADA.
                .append("cantidadesPorEstado", new Document("INGRESADA", 4L)));

        mongoTemplate.getCollection("donaciones").insertMany(java.util.List.of(
                new Document("_id", "1").append("donadorId", "2").append("estado", "CONQUEJA"),
                new Document("_id", "2").append("donadorId", "2").append("estado", "CONQUEJA"),
                new Document("_id", "3").append("donadorId", "2").append("estado", "ACEPTADA"),
                new Document("_id", "4").append("donadorId", "2").append("estado", "INGRESADA")));

        DonadorDoc reporte = service().obtenerReporteDonador("2");

        assertThat(reporte.getCantidadesPorEstado())
                .containsOnly(
                        org.assertj.core.api.Assertions.entry("CONQUEJA", 2L),
                        org.assertj.core.api.Assertions.entry("ACEPTADA", 1L),
                        org.assertj.core.api.Assertions.entry("INGRESADA", 1L));
    }
}
