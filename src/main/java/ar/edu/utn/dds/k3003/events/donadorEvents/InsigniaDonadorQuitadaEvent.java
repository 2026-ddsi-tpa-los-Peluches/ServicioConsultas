package ar.edu.utn.dds.k3003.events.donadorEvents;



import lombok.Getter;

import java.time.Instant;


@Getter
public class InsigniaDonadorQuitadaEvent {
    private String donadorId;
    private String insigniaId;
    private Instant fechaEvento;

    public InsigniaDonadorQuitadaEvent(String donadorId, String insigniaId) {
        this.donadorId = donadorId;
        this.insigniaId = insigniaId;
        this.fechaEvento = Instant.now();
    }
}