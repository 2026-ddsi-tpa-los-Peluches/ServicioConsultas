package ar.edu.utn.dds.k3003.dtos;

import java.util.List;
import java.util.Map;

public record DepositoReporteDTO(
        String nombre,
        String direccion,
        String algoritmoMatchmaking,
        Integer capacidadMaxima,
        Integer stockTotal,
        Double ocupacionPorcentaje,
        Map<String, Long> cantidadAsignacionesPorEstado,
        Integer cantidadDeEntidadesDistintasAtendidas,
        Double tiempoPromedioEntregaMilis
) {}