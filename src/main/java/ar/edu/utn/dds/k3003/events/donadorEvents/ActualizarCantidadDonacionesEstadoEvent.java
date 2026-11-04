package ar.edu.utn.dds.k3003.events.donadorEvents;

public record ActualizarCantidadDonacionesEstadoEvent(
    String donadorID,
    String estado
) {}