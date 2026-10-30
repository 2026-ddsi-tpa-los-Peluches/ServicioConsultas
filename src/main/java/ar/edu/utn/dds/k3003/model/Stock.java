package ar.edu.utn.dds.k3003.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("stock")
public record Stock(
        @Id String id,              // productoId + "-" + depositoId
        @Indexed String productoId,
        String depositoId,
        String nombreDeposito,
        int cantidad,
        Instant ultimaActualizacion
) {}