package ar.edu.utn.dds.k3003.events.donadorEvents;
import java.time.Instant;

public record ActualizarCantidadDonacionesEstadoEvent(
        String donadorID,
        String estadoAnterior, // Puede ser null si es la creación inicial
        String estadoNuevo,    // Estado actual o nuevo de la donación
        Instant fechaEvento
) {
    // Constructor auxiliar por si querés instanciarlo rápido sin pasar la fecha manualmente
    public ActualizarCantidadDonacionesEstadoEvent(String donadorID, String estadoAnterior, String estadoNuevo) {
        this(donadorID, estadoAnterior, estadoNuevo, Instant.now());
    }
}