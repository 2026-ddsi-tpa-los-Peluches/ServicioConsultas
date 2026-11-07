package ar.edu.utn.dds.k3003.events.donadorEvents;

import lombok.Getter;
import java.time.Instant;

@Getter
public class DonadorInsigniaAgregadaEvent {
    private String donadorId;
    private String insigniaId;
    private String nombre;
    private String descripcion;
    private Instant fechaEvento;

    public DonadorInsigniaAgregadaEvent(String donadorId, String insigniaId, String nombre, String descripcion) {
        this.donadorId = donadorId;
        this.insigniaId = insigniaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = Instant.now();
    }
}