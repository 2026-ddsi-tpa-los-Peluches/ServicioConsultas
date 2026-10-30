package ar.edu.utn.dds.k3003.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("necesidades")
public record Necesidad(
        @Id String id,
        String entidadId,
        String razonSocial,
        int nivelUrgencia,
        String tipoNecesidad,
        String productoId,
        String nombreProducto,
        String categoriaProducto,
        String subcategoriaProducto,
        int cantidadFaltante,
        List<AsignacionPendiente> asignacionesPendientes,
        Instant ultimaActualizacion
) {
    public record AsignacionPendiente(String asignacionId, String depositoId, int unidades) {}
}