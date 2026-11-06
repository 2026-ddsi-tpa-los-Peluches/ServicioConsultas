package ar.edu.utn.dds.k3003.events.categoriaEvents;

import java.time.Instant;

public record CategoriaCreadaEvent(
        String id,
        String nombre,
        String descripcion,
        Instant fechaEvento
) {}