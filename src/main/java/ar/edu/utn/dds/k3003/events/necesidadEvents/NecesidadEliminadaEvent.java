package ar.edu.utn.dds.k3003.events.necesidadEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class NecesidadEliminadaEvent {
    private String id;
    private Instant fechaEvento;

    public NecesidadEliminadaEvent(String id) {
        this.id = id;
        this.fechaEvento = Instant.now();
    }
}