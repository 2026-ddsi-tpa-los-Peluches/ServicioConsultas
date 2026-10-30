package ar.edu.utn.dds.k3003.events;

import ar.edu.utn.dds.k3003.model.Donador;

import java.time.Instant;
import java.util.List;

public record DonadorIncentivosActualizadoEvent(
        String donadorId, String categoriaActual,
        List<Donador.CategoriaHistorial> historialCategorias,
        List<Donador.Insignia> insignias,
        Donador.MisionActual misionActual,
        Instant fechaEvento) {}