package ar.edu.utn.dds.k3003.events.categoriaEvents;

import java.time.Instant;

public record CategoriaEliminadaEvent(
        String id,
        Instant fechaEvento
) {}