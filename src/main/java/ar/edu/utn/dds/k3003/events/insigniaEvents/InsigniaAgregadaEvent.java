package ar.edu.utn.dds.k3003.events.insigniaEvents;


import lombok.Getter;

import java.time.Instant;

@Getter
public class InsigniaAgregadaEvent {
    private Integer insigniaId;
    private String nombre;
    private String descripcion;
    private Instant fechaEvento;

    public InsigniaAgregadaEvent(Integer insigniaId, String nombre, String descripcion) {
        this.insigniaId = insigniaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = Instant.now();
    }
}