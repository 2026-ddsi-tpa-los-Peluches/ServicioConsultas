package ar.edu.utn.dds.k3003.dtos;

import java.time.LocalDateTime;

public record MisionHistoricaDTO(
    String misionId,
    String nombre,
    LocalDateTime completadaEn
) {}