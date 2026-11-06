package ar.edu.utn.dds.k3003.dtos.donadoresYEntidades;

public record NecesidadMaterialDTO(
    Integer id,
    String entidadID,
    Integer nivelDeUrgencia,
    String descripcion,
    Integer cantidadObjetivo,
    Integer cantidadRecibida,
    String productoSolicitadoID,
    TipoNecesidadMaterialEnum tipo) {}
