package ar.edu.utn.dds.k3003.events.donadorEvents;


import lombok.Getter;

import java.time.Instant;

@Getter
public class InsigniaDonadorAgregadaEvent {
    private String donadorId;
    private String insigniaId;
    private Instant fechaEvento;

    public InsigniaDonadorAgregadaEvent(String donadorId, String insigniaId) {
        this.donadorId = donadorId;
        this.insigniaId = insigniaId;
        this.fechaEvento = Instant.now();
    }

}