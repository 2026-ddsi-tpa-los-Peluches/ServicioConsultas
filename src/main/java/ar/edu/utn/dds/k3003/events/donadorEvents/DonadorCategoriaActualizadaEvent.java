package ar.edu.utn.dds.k3003.events.donadorEvents;

import java.time.Instant;

public record DonadorCategoriaActualizadaEvent(
        String donadorId,
        String nuevaCategoria,
        Instant fechaCambio
) {}