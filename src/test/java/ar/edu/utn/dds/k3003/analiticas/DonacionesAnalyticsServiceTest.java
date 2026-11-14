package ar.edu.utn.dds.k3003.analiticas;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.dds.k3003.dtos.ReporteCalidadYDonadoresDTO;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class DonacionesAnalyticsServiceTest {

    @Autowired private MongoTemplate mongoTemplate;

    // El contexto (y el Mongo embebido) se comparte entre clases de test: hay que limpiar para que
    // donadores/donaciones de otro test no se mezclen con los de este.
    @BeforeEach
    void limpiarColecciones() {
        mongoTemplate.getDb().getCollection("donadores").deleteMany(new Document());
        mongoTemplate.getDb().getCollection("donaciones").deleteMany(new Document());
    }

    private DonacionesAnalyticsService service() {
        return new DonacionesAnalyticsService(mongoTemplate);
    }

    // Bug 11/12: el ranking no debe incluir donadores con 0 quejas, y tiene que traer nombre/apellido.
    @Test
    void rankingExcluyeDonadoresSinQuejasYTraeNombreYApellido() {
        mongoTemplate.getCollection("donadores").insertMany(List.of(
                new Document("_id", "1").append("nombre", "Lucía").append("apellido", "Fernández")
                        .append("estado", "VERIFICADO").append("categoriaActual", "Ocasional")
                        .append("quejas", List.of(new Document("quejaId", 3))),
                new Document("_id", "3").append("nombre", "Sin").append("apellido", "Quejas")
                        .append("estado", "SOSPECHOSO").append("categoriaActual", "Ocasional")
                        .append("quejas", List.of())));

        ReporteCalidadYDonadoresDTO reporte = service().obtenerReporteCompleto(10);

        assertThat(reporte.donadoresConMasQuejas()).hasSize(1);
        Document unico = reporte.donadoresConMasQuejas().get(0);
        assertThat(unico.getString("donadorId")).isEqualTo("1");
        assertThat(unico.getString("nombre")).isEqualTo("Lucía");
        assertThat(unico.getString("apellido")).isEqualTo("Fernández");
        assertThat(unico.getInteger("cantidadQuejas")).isEqualTo(1);
    }
}
