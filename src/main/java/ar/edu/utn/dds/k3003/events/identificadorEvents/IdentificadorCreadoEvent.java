package ar.edu.utn.dds.k3003.events.identificadorEvents;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import java.time.Instant;

public record IdentificadorCreadoEvent(
        String id,
        TipoIdentificadorEnum tipo,
        String descripcion,
        Instant fechaEvento
) {}