package ar.edu.utn.dds.k3003.events.donadorEvents;

import lombok.Getter;

import java.time.Instant;

@Getter
public class MisionDonadorCompletadaEvent {
    // Getters
    private String donadorId;
    private MisionHistoricaDTO misionCompletada; // La que pasa al historial
    private Instant fechaEvento;

    // Constructor completo
    public MisionDonadorCompletadaEvent(String donadorId, MisionHistoricaDTO misionCompletada) {
        this.donadorId = donadorId;
        this.misionCompletada = misionCompletada;
        this.fechaEvento = Instant.now();
    }
}