package ar.edu.utn.dds.k3003.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("asignaciones")
public record AsignacionDoc(
        @Id String id,
        String asignacionRealizadaPor,
        String paqueteId,
        String necesidadId,
        String depositoId,
        String donacionId,
        String entidadId,
        int unidades,

        String estado,                 // ASIGNADA, COMPLETADA, etc.

        Instant fechaAsignacion,
        Instant ultimaActualizacion
) {}
