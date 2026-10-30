package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Asignacion;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.Necesidad;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResumenDonador {

    private final MongoTemplate mongo;

    public ResumenDonador(MongoTemplate mongo) { this.mongo = mongo; }

    public synchronized void recalcular(String donadorId) {
        // donaciones por estado
        Map<String, Integer> porEstado = new HashMap<>();
        List<Document> grupos = mongo.getCollection("donaciones").aggregate(List.of(
                new Document("$match", new Document("donadorId", donadorId)),
                new Document("$group", new Document("_id", "$estado")
                        .append("cantidad", new Document("$sum", 1)))
        )).into(new ArrayList<>());
        for (Document g : grupos) {
            porEstado.put(g.getString("_id"), g.getInteger("cantidad"));
        }

        // quejas recibidas
        Document total = mongo.getCollection("donaciones").aggregate(List.of(
                new Document("$match", new Document("donadorId", donadorId)),
                new Document("$unwind", "$quejas"),
                new Document("$count", "total")
        )).first();
        int quejas = total == null ? 0 : total.getInteger("total");

        // entidades que recibieron sus donaciones (solo entregas completadas)
        List<String> donacionIds = mongo.find(
                        new Query(Criteria.where("donadorId").is(donadorId)), Donacion.class)
                .stream().map(Donacion::id).toList();
        List<Asignacion> entregadas = mongo.find(
                new Query(Criteria.where("donacionId").in(donacionIds)
                        .and("estado").is("COMPLETADA")), Asignacion.class);

        Map<String, Integer> unidadesPorEntidad = new HashMap<>();
        for (Asignacion a : entregadas) {
            unidadesPorEntidad.merge(a.entidadId(), a.unidades(), Integer::sum);
        }
        List<Donador.EntidadBeneficiada> entidades = unidadesPorEntidad.entrySet().stream()
                .map(en -> new Donador.EntidadBeneficiada(
                        en.getKey(), razonSocial(en.getKey()), en.getValue()))
                .toList();

        mongo.upsert(
                new Query(Criteria.where("_id").is(donadorId)),
                new Update()
                        .set("donacionesPorEstado", porEstado)
                        .set("quejasRecibidas", quejas)
                        .set("entidades", entidades)
                        .set("resumenActualizadoEn", Instant.now()),
                Donador.class);
    }

    private String razonSocial(String entidadId) {
        Necesidad n = mongo.findOne(
                new Query(Criteria.where("entidadId").is(entidadId)), Necesidad.class);
        return n == null ? null : n.razonSocial();
    }
}