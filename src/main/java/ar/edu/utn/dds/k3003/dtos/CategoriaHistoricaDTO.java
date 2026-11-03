package ar.edu.utn.dds.k3003.dtos;

import java.time.Instant;
import java.time.LocalDateTime;

public record CategoriaHistoricaDTO(
        String categoria,
        Instant fecha
) {}