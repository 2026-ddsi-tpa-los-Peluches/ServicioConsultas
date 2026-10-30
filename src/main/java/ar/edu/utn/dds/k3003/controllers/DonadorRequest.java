package ar.edu.utn.dds.k3003.controllers;

public record DonadorRequest(
        String depositoID,
        String donacionID,
        String productoID,
        Integer cantidad
) {}