package ar.edu.utn.dds.k3003.analiticas;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class NecesidadAnalyticsServiceTest {

    @Autowired private MongoTemplate mongoTemplate;

    // El contexto (y el Mongo embebido) se comparte entre clases de test: hay que limpiar para que
    // necesidades/asignaciones/productos de otro test no se mezclen con los de este.
    @BeforeEach
    void limpiarColecciones() {
        mongoTemplate.getDb().getCollection("necesidades").deleteMany(new Document());
        mongoTemplate.getDb().getCollection("asignaciones").deleteMany(new Document());
        mongoTemplate.getDb().getCollection("productos").deleteMany(new Document());
    }

    private NecesidadAnalyticsService service() {
        return new NecesidadAnalyticsService(mongoTemplate);
    }

    // Bug 5: la necesidad tiene una asignación ASIGNADA (todavía no entregada) y asignacionesPendientes
    // tiene que verla, aunque el array embebido de la propia necesidad nunca se haya actualizado.
    @Test
    void asignacionesPendientesIncluyeLasAsignacionesEnEstadoAsignada() {
        mongoTemplate.getCollection("necesidades").insertOne(new Document("_id", "1")
                .append("entidadId", "1")
                .append("razonSocial", "Comedor Los Peluches")
                .append("nivelUrgencia", 8)
                .append("descripcion", "Arroz para el comedor diario")
                .append("tipoNecesidad", "RECURRENTE")
                .append("productoId", "1")
                .append("nombreProducto", "Arroz 1kg")
                .append("categoriaProducto", "1")
                .append("subcategoriaProducto", null)
                .append("cantidadObjetivo", 200)
                .append("cantidadRecibida", 0));

        mongoTemplate.getCollection("asignaciones").insertOne(new Document("_id", "5")
                .append("necesidadId", "1")
                .append("depositoId", "1")
                .append("unidades", 50)
                .append("estado", "ASIGNADA"));

        List<Document> reporte = service().obtenerReporteNecesidadesInsatisfechas(null, null, null, null);

        assertThat(reporte).hasSize(1);
        List<Document> pendientes = reporte.get(0).getList("asignacionesPendientes", Document.class);
        assertThat(pendientes).hasSize(1);
        assertThat(pendientes.get(0).getString("asignacionId")).isEqualTo("5");
        assertThat(pendientes.get(0).getInteger("unidades")).isEqualTo(50);
    }

    // Bug 6/7: la subcategoría se resuelve desde el producto (igual que la categoría), y el filtro
    // de categoría acepta tanto el id como el nombre.
    @Test
    void categoriaYSubcategoriaSeResuelvenDesdeElProductoYElFiltroAceptaIdONombre() {
        mongoTemplate.getCollection("productos").insertOne(new Document("productoId", "4")
                .append("categoriaId", "3")
                .append("categoriaNombre", "Ropa")
                .append("subcategoriaId", "5")
                .append("subcategoriaNombre", "Abrigo"));

        mongoTemplate.getCollection("necesidades").insertOne(new Document("_id", "2")
                .append("entidadId", "2")
                .append("razonSocial", "Hogar Santa Clara")
                .append("nivelUrgencia", 10)
                .append("descripcion", "Frazadas por ola polar")
                .append("tipoNecesidad", "EXTRAORDINARIA")
                .append("productoId", "4")
                .append("nombreProducto", "Frazada de polar")
                .append("categoriaProducto", "3")
                .append("subcategoriaProducto", null)
                .append("cantidadObjetivo", 50)
                .append("cantidadRecibida", 40));

        List<Document> porNombre = service()
                .obtenerReporteNecesidadesInsatisfechas(null, null, "Ropa", null);
        List<Document> porId = service()
                .obtenerReporteNecesidadesInsatisfechas(null, null, "3", null);

        assertThat(porNombre).hasSize(1);
        assertThat(porNombre.get(0).getString("categoriaProducto")).isEqualTo("Ropa");
        assertThat(porNombre.get(0).getString("subcategoriaProducto")).isEqualTo("Abrigo");
        assertThat(porId).hasSize(1);
    }
}
