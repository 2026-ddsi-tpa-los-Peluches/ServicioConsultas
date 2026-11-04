package ar.edu.utn.dds.k3003.events.donadorEvents;


import java.time.LocalDate;

public record QuejaDonadorRegistradaEvent(
        Integer quejaId,
        String donacionId,
        String donadorId,
        String motivo,
        LocalDate fecha
) {}