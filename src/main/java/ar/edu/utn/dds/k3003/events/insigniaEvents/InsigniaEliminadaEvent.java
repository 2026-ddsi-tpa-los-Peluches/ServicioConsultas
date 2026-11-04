package ar.edu.utn.dds.k3003.events.insigniaEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class InsigniaEliminadaEvent {
    private Integer insigniaId;
    private Instant fechaEvento;

    public InsigniaEliminadaEvent(Integer insigniaId) {
        this.insigniaId = insigniaId;
        this.fechaEvento = Instant.now();
    }

}