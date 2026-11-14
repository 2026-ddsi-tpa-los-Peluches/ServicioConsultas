package ar.edu.utn.dds.k3003.events.donacionEvents;

import java.time.Instant;

public record DonacionEliminadaEvent(String donacionId, Instant fechaEvento) {
    public DonacionEliminadaEvent(String donacionId) {
        this(donacionId, Instant.now());
    }
}