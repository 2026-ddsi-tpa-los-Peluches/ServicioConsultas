package ar.edu.utn.dds.k3003.events.donadorEvents;

import java.time.Instant;
import java.time.LocalDateTime;

public record DonadorCategoriaActualizadaEvent(
        String donadorId,
        String nuevaCategoria,
        Instant fechaCambio
) {}