package ar.edu.utn.dds.k3003.events.donacionEvents;

import java.time.Instant;

public record DonacionEstadoActualizadoEvent(
        String donacionId,
        String nuevoEstado,
        Instant fechaEvento
) {}