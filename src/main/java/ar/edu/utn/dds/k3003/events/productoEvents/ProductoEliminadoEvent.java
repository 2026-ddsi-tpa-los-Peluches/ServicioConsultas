package ar.edu.utn.dds.k3003.events.productoEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class ProductoEliminadoEvent {
    private String productoId;
    private Instant fechaEvento;

    public ProductoEliminadoEvent(String productoId) {
        this.productoId = productoId;
        this.fechaEvento = Instant.now();
    }
}