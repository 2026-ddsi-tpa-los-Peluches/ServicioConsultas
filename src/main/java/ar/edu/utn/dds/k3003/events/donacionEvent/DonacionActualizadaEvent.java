package ar.edu.utn.dds.k3003.events.donacionEvent;

import ar.edu.utn.dds.k3003.documentos.DonacionDoc;

import java.time.Instant;
import java.util.List;

public record DonacionActualizadaEvent(
        String donacionId, String donadorId, String productoId, String nombreProducto,
        String categoriaProducto, String subcategoriaProducto, int cantidad, String estado,
        Instant fechaIngreso, List<DonacionDoc.Queja> quejas, Instant fechaEvento) {}