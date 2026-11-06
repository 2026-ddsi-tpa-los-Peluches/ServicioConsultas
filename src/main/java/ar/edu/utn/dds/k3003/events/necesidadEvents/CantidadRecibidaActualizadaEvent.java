package ar.edu.utn.dds.k3003.events.necesidadEvents;

import lombok.Getter;

import java.time.Instant;



@Getter
public class CantidadRecibidaActualizadaEvent {
    private String necesidadId;
    private int cantidad;
    private Instant fechaEvento;

    public CantidadRecibidaActualizadaEvent(String necesidadId, int cantidad) {
        this.necesidadId = necesidadId;
        this.cantidad = cantidad;
        this.fechaEvento = Instant.now();
    }
}