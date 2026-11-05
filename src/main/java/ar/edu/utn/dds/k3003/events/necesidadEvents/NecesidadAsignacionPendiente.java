package ar.edu.utn.dds.k3003.events.necesidadEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class NecesidadAsignacionPendiente {
    private String asignacionId;
    private String necesidadId;
    private String depositoId;
    private int unidades;
    private Instant fechaEvento;

    public NecesidadAsignacionPendiente(String asignacionId, String necesidadId, String depositoId, int unidades) {
        this.asignacionId = asignacionId;
        this.necesidadId = necesidadId;
        this.depositoId = depositoId;
        this.unidades = unidades;
        this.fechaEvento = Instant.now();
    }

}