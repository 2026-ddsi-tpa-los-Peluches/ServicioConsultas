package ar.edu.utn.dds.k3003.listeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import ar.edu.utn.dds.k3003.events.donadorEvents.DonadorCreadoEvent;
import ar.edu.utn.dds.k3003.service.Actualizador;
import ar.edu.utn.dds.k3003.service.ResumenDonador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@DataMongoTest
class ConsultasListenerTest {

    @Autowired private MongoTemplate mongoTemplate;

    // El contexto (y el Mongo embebido) se comparte entre clases de test: hay que limpiar para que
    // un donador insertado por otro test no quede "ya existente" y pise el setOnInsert.
    @BeforeEach
    void limpiarColecciones() {
        mongoTemplate.getDb().getCollection("donadores").deleteMany(new org.bson.Document());
    }

    // Bug 9: el alta de un donador tiene que sembrar su categoría inicial en el historial, para que
    // no quede en null (si nunca cambia de categoría) ni falte esa primera entrada (si sí cambia).
    @Test
    void altaDeDonadorSiembraLaCategoriaInicialEnElHistorial() {
        ConsultasListener listener = new ConsultasListener(
                new Actualizador(mongoTemplate), mock(ResumenDonador.class), mongoTemplate);

        DonadorCreadoEvent evento = new DonadorCreadoEvent(
                1, "Lucía", "Fernández", 28, "lucia@example.com", "38111222",
                "Monroe 5000, CABA", "VERIFICADO", "Ocasional");

        listener.onDonadorCreado(evento);

        DonadorDoc donador = mongoTemplate.findOne(
                new Query(Criteria.where("_id").is("1")), DonadorDoc.class);

        assertThat(donador).isNotNull();
        assertThat(donador.getHistorialCategorias()).hasSize(1);
        assertThat(donador.getHistorialCategorias().get(0).getCategoria()).isEqualTo("Ocasional");
        assertThat(donador.getHistorialCategorias().get(0).getFecha()).isNotNull();
    }
}
