package ar.edu.utn.dds.k3003.events;

import java.time.Instant;

public record NecesidadActualizadaEvent(
        String necesidadId, String entidadId, String razonSocial, int nivelUrgencia,
        String tipoNecesidad, String productoId, String nombreProducto,
        String categoriaProducto, String subcategoriaProducto,
        int cantidadFaltante, Instant fechaEvento) {}