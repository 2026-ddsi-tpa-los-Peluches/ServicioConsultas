package ar.edu.utn.dds.k3003.dtos.donadoresYEntidades;

import java.util.List;

public record DonadorStatsDTO(
    Integer id,
    String nombre,
    String apellido,
    Integer edad,
    EstadoDonadorEnum estado,
    String categoria,
    String misionActualID,
    List<String> insigniasID) {}
