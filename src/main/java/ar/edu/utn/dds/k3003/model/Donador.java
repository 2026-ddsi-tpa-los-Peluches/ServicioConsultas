package ar.edu.utn.dds.k3003.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Document("donadores")
public record Donador(
        @Id String id,
        String estado,
        Instant estadoActualizadoEn,
        String categoriaActual,
        List<CategoriaHistorial> historialCategorias,
        List<Insignia> insignias,
        MisionActual misionActual,
        Instant incentivosActualizadoEn,
        Map<String, Integer> donacionesPorEstado,
        int quejasRecibidas,
        List<EntidadBeneficiada> entidades,
        Instant resumenActualizadoEn
) {
  public record CategoriaHistorial(String categoria, LocalDate desde) {}
  public record Insignia(String insigniaId, String nombre) {}
  public record MisionActual(String misionId, String nombre, int progreso, int objetivo) {}
  public record EntidadBeneficiada(String entidadId, String razonSocial, int unidades) {}
}