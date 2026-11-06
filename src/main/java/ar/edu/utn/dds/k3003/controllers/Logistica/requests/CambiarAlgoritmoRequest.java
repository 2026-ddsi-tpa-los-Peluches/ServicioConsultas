package ar.edu.utn.dds.k3003.controllers.Logistica.requests;

import ar.edu.utn.dds.k3003.catedra.dtos.logistica.TipoAlgoritmoEnum;

public record CambiarAlgoritmoRequest(
        TipoAlgoritmoEnum algoritmo
) {}