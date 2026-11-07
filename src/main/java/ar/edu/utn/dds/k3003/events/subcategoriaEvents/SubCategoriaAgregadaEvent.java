package ar.edu.utn.dds.k3003.events.subcategoriaEvents;

import lombok.Getter;
import java.time.Instant;

@Getter
public class SubCategoriaAgregadaEvent {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoriaId;
    private Instant fechaEvento;

    public SubCategoriaAgregadaEvent(Long id, String nombre, String descripcion, String categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.fechaEvento = Instant.now();
    }
}