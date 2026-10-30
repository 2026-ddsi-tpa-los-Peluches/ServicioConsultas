package ar.edu.utn.dds.k3003.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("asignaciones")
public record Asignacion(
        @Id String id,
        String paqueteId,
        String necesidadId,
        String depositoId,
        String donacionId,
        String entidadId,
        int unidades,
        String estado,                 // ASIGNADA, COMPLETADA
        Instant fechaAsignacion,
        Instant fechaIngresoDonacion,
        Instant fechaEntrega,          // null hasta que se completa
        Instant ultimaActualizacion
) {}