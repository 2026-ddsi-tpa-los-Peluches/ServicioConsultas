package ar.edu.utn.dds.k3003.events.donadorEvents;

import java.time.Instant;

public record DonadorEstadoActualizadoEvent(String donadorId, String estado, Instant fechaEvento) {}