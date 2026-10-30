package ar.edu.utn.dds.k3003.events;

import java.time.Instant;

public record DonadorEstadoActualizadoEvent(String donadorId, String estado, Instant fechaEvento) {}