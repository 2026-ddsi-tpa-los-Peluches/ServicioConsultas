package ar.edu.utn.dds.k3003.config;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Limpieza idempotente de datos que quedaron con un formato viejo en Mongo. Corre en cada arranque,
 * no hace nada si no hay documentos para corregir y nunca impide que el servicio levante.
 */
@Component
public class MigracionDatosAlArranque implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MigracionDatosAlArranque.class);

    private final MongoTemplate mongo;

    public MigracionDatosAlArranque(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public void run(ApplicationArguments args) {
        ejecutar("depositos", this::migrarDepositos);
        ejecutar("insignias de donadores", this::migrarInsigniasDeDonadores);
    }

    private void ejecutar(String nombre, Runnable paso) {
        try {
            paso.run();
        } catch (Exception e) {
            log.warn("Migración '{}' omitida por error", nombre, e);
        }
    }

    // paquetesDeposito tiene que ser una lista: antes un $inc lo dejaba como número (rompía findAll
    // de /depositos) y los paquetes se pusheaban a "stockActual", un campo que el documento no mapea.
    private void migrarDepositos() {
        MongoCollection<Document> depositos = mongo.getCollection("depositos");

        for (Document doc : depositos.find(new Document("stockActual", new Document("$type", "array")))) {
            Object paquetes = doc.get("stockActual");
            Object actuales = doc.get("paquetesDeposito");
            List<Object> unidos = new ArrayList<>();
            if (actuales instanceof List<?> l) unidos.addAll(l);
            if (paquetes instanceof List<?> l) unidos.addAll(l);
            depositos.updateOne(new Document("_id", doc.get("_id")),
                    new Document("$set", new Document("paquetesDeposito", unidos))
                            .append("$unset", new Document("stockActual", "")));
            log.info("Depósito {}: stockActual movido a paquetesDeposito", doc.get("_id"));
        }

        long arreglados = depositos.updateMany(
                new Document("paquetesDeposito",
                        new Document("$exists", true).append("$not", new Document("$type", "array"))),
                new Document("$set", new Document("paquetesDeposito", new ArrayList<>()))).getModifiedCount();
        if (arreglados > 0) {
            log.info("{} depósito(s) con paquetesDeposito no-lista reiniciados", arreglados);
        }
    }

    // donadores.insignias tiene que ser una lista de {_id, nombre, descripcion}. Los elementos que
    // quedaron como String (formato anterior) se completan con el catálogo y, al revés, lo que el
    // donador ya tenía embebido sirve para reponer el catálogo si faltaba.
    private void migrarInsigniasDeDonadores() {
        MongoCollection<Document> donadores = mongo.getCollection("donadores");
        MongoCollection<Document> catalogo = mongo.getCollection("insignias");

        for (Document donador : donadores.find(new Document("insignias", new Document("$exists", true)))) {
            if (!(donador.get("insignias") instanceof List<?> insignias)) continue;

            boolean cambio = false;
            List<Object> nuevas = new ArrayList<>();
            for (Object elemento : insignias) {
                if (elemento instanceof String id) {
                    Document entrada = new Document("_id", id);
                    Document delCatalogo = buscarEnCatalogo(catalogo, id);
                    if (delCatalogo != null) {
                        entrada.append("nombre", delCatalogo.get("nombre"))
                               .append("descripcion", delCatalogo.get("descripcion"));
                    }
                    nuevas.add(entrada);
                    cambio = true;
                } else {
                    nuevas.add(elemento);
                    if (elemento instanceof Document emb) {
                        reponerCatalogo(catalogo, emb);
                    }
                }
            }
            if (cambio) {
                donadores.updateOne(new Document("_id", donador.get("_id")),
                        new Document("$set", new Document("insignias", nuevas)));
                log.info("Donador {}: insignias en formato viejo convertidas", donador.get("_id"));
            }
        }
    }

    private Document buscarEnCatalogo(MongoCollection<Document> catalogo, String id) {
        try {
            return catalogo.find(new Document("_id", Integer.valueOf(id))).first();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void reponerCatalogo(MongoCollection<Document> catalogo, Document embebida) {
        Object id = embebida.get("_id");
        if (id == null || embebida.get("nombre") == null) return;
        try {
            Integer insigniaId = Integer.valueOf(String.valueOf(id));
            if (catalogo.countDocuments(new Document("_id", insigniaId)) == 0) {
                catalogo.insertOne(new Document("_id", insigniaId)
                        .append("nombre", embebida.get("nombre"))
                        .append("descripcion", embebida.get("descripcion")));
                log.info("Catálogo: insignia {} repuesta desde un donador", insigniaId);
            }
        } catch (NumberFormatException ignored) {
            // id no numérico: no es una insignia del catálogo
        }
    }
}
