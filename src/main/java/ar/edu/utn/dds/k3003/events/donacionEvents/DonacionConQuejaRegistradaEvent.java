package ar.edu.utn.dds.k3003.events.donacionEvents;

import java.time.Instant;
import java.time.LocalDate;

public record DonacionConQuejaRegistradaEvent(
        String donacionId,
        String quejaId,
        LocalDate fechaQueja,
        String descripcionQueja,
        String nuevoEstado, // "CONQUEJA"
        Instant fechaEvento
) {}