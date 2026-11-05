package ar.edu.utn.dds.k3003.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document("donaciones")
public record DonacionDoc(
        @Id String id,

        String donadorId,
        String productoId,
        String nombreProducto,
        String categoriaProducto,
        String subcategoriaProducto,
        int cantidad,

        String estado,              // INGRESADA, ACEPTADA, CONQUEJA

        Instant fechaIngreso,
        Queja queja,
        Instant ultimaActualizacion
) {
    public record Queja(String quejaId, LocalDate fecha, String descripcion) {}
}