package ar.edu.utn.dds.k3003.events.donacionEvents;

import java.time.Instant;

public record DonacionCreadaEvent(
        String donacionId,
        String donadorId,
        String productoId,
        String nombreProducto,
        String categoriaProducto,
        String subcategoriaProducto,
        int cantidad,
        String estado,
        Instant fechaIngreso,
        Instant ultimaActualizacion
) {}