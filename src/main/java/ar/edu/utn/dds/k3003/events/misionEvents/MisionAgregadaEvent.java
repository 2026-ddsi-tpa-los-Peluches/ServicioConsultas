package ar.edu.utn.dds.k3003.events.misionEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class MisionAgregadaEvent {
    private Integer misionId;
    private String nombre;
    private String insigniaId;
    private String categoriaInicio;
    private String categoriaFin;
    private String tipoMision;
    private Instant fechaEvento;

    public MisionAgregadaEvent(Integer misionId, String nombre, String insigniaId,
                               String categoriaInicio, String categoriaFin,
                               String tipoMision) {
        this.misionId = misionId;
        this.nombre = nombre;
        this.insigniaId = insigniaId;
        this.categoriaInicio = categoriaInicio;
        this.categoriaFin = categoriaFin;
        this.tipoMision = tipoMision;
        this.fechaEvento = Instant.now();
    }
}