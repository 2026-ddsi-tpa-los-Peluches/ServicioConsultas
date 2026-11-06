package ar.edu.utn.dds.k3003.events.entidadEvents;

import java.time.Instant;

public record EntidadBeneficaActualizadaEvent(
        Integer id,
        String razonSocial,
        String domicilio,
        String telefono,
        String correo,
        Instant fechaEvento
) {}