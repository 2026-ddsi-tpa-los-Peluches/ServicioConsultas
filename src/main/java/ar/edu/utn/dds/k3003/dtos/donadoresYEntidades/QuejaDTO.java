package ar.edu.utn.dds.k3003.dtos.donadoresYEntidades;

import java.time.LocalDate;

public record QuejaDTO(
    Integer id, String donacionID, String donadorID, LocalDate fecha, String descripcion) {}
