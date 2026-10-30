package ar.edu.utn.dds.k3003.events;

import ar.edu.utn.dds.k3003.model.Donacion;

import java.time.Instant;
import java.util.List;

public record DonacionActualizadaEvent(
        String donacionId, String donadorId, String productoId, String nombreProducto,
        String categoriaProducto, String subcategoriaProducto, int cantidad, String estado,
        Instant fechaIngreso, List<Donacion.Queja> quejas, Instant fechaEvento) {}