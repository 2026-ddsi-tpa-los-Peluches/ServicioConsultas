package ar.edu.utn.dds.k3003.events.misionEvents;



import lombok.Getter;
import java.time.Instant;

@Getter
public class MisionEliminadaEvent {
    private String misionId;
    private Instant fechaEvento;

    public MisionEliminadaEvent(String misionId) {
        this.misionId = misionId;
        this.fechaEvento = Instant.now();
    }
}