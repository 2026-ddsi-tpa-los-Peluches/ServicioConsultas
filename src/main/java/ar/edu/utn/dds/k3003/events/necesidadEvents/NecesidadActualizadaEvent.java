package ar.edu.utn.dds.k3003.events.necesidadEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class NecesidadActualizadaEvent {
    private String id;
    private int nivelUrgencia;
    private String descripcion;
    private Instant fechaEvento;

    public NecesidadActualizadaEvent(String id, int nivelUrgencia, String descripcion) {
        this.id = id;
        this.nivelUrgencia = nivelUrgencia;
        this.descripcion = descripcion;
        this.fechaEvento = Instant.now();
    }
}