package ar.edu.utn.dds.k3003.events.donadorEvents;

import java.time.Instant;

public class MisionDonadorLimpiadaEvent {
    private String donadorId;
    private Instant fechaEvento;

    public MisionDonadorLimpiadaEvent(String donadorId) {
        this.donadorId = donadorId;
        this.fechaEvento = Instant.now();
    }

    public String getDonadorId() { return donadorId; }
    public Instant getFechaEvento() { return fechaEvento; }
}