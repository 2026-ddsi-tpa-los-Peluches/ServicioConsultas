package ar.edu.utn.dds.k3003.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("necesidades")
public record NecesidadDoc(
        @Id String id,

        //entidad
        String entidadId,
        String razonSocial,


        //cosas de la necesidad
        int nivelUrgencia,
        String descripcion,
        String tipoNecesidad,


        //producto
        String productoId,
        String nombreProducto,
        String categoriaProducto,
        String subcategoriaProducto,

        //cantidades
        int cantidadObjetivo,
        int cantidadRecibida,

        //asignaciones pendientes a la necesidad
        List<AsignacionPendienteDoc> asignacionesPendientes,

        Instant ultimaActualizacion
) {
    public record AsignacionPendienteDoc(String asignacionId, String depositoId, int unidades) {}
}