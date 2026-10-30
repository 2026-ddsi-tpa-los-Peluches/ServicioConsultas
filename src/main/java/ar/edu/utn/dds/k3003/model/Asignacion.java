package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


