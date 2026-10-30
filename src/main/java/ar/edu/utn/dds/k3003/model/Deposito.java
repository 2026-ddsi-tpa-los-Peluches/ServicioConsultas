package ar.edu.utn.dds.k3003.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("depositos")
public record Deposito(
        @Id String id,
        String nombre,
        int capacidad,
        int stockTotal,
        String algoritmoMatchmaking,
        Instant ultimaActualizacion
) {}