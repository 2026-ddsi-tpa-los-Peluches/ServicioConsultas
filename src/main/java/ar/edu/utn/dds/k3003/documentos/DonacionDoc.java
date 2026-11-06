package ar.edu.utn.dds.k3003.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document("donaciones")
public record DonacionDoc(
        @Id String id,

        // atributos generales de una donacion
        String donadorId,
        String productoId,
        String nombreProducto,
        String categoriaProducto,
        String subcategoriaProducto,
        int cantidad,


        // estado de una donacion
        String estado,              // INGRESADA, ACEPTADA, CONQUEJA


        // ingreso de una donacion
        Instant fechaIngreso,

        // queja de una donacion
        QuejaDonacionDoc queja,


        Instant ultimaActualizacion
) {
    public record QuejaDonacionDoc(String quejaId, LocalDate fecha, String descripcion) {}
}