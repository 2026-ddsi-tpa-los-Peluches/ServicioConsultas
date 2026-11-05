package ar.edu.utn.dds.k3003.events.donadorEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class DonadorMisionEvent {
    private String donadorId;
    private String misionActualId;

    public DonadorMisionEvent(String donadorId,String misionActualId) {
        this.donadorId = donadorId;
        this.misionActualId = misionActualId;
    }
}
