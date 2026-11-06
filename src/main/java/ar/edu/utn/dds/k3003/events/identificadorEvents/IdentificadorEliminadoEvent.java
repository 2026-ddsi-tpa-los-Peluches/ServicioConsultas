package ar.edu.utn.dds.k3003.events.identificadorEvents;

import java.time.Instant;

public record IdentificadorEliminadoEvent(
        String id,
        Instant fechaEvento
) {}