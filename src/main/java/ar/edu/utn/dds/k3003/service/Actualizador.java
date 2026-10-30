package ar.edu.utn.dds.k3003.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class Actualizador {

    private final MongoTemplate mongo;

    public Actualizador(MongoTemplate mongo) { this.mongo = mongo; }

    /** Devuelve true si aplicó el cambio, false si el aviso era viejo o repetido. */
    public boolean aplicarSiEsMasNuevo(Class<?> clase, String id, String campoFecha,
                                       Instant fechaEvento, Update cambios) {
        Query q = new Query(Criteria.where("_id").is(id).orOperator(
                Criteria.where(campoFecha).lt(fechaEvento),
                Criteria.where(campoFecha).exists(false)));
        cambios.set(campoFecha, fechaEvento);
        try {
            mongo.upsert(q, cambios, clase);
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }
}