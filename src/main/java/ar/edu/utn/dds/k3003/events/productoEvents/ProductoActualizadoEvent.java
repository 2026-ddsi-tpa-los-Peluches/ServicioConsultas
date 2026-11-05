package ar.edu.utn.dds.k3003.events.productoEvents;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ProductoActualizadoEvent {
    private String productoId;
    private String nombre;
    private String descripcion;
    private Instant fechaEvento;

    public ProductoActualizadoEvent(String productoId, String nombre, String descripcion) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = Instant.now();
    }
}