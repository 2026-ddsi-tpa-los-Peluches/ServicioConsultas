package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.documentos.AsignacionDoc;
import ar.edu.utn.dds.k3003.documentos.PaqueteDoc;
import ar.edu.utn.dds.k3003.dtos.logistica.*;
import ar.edu.utn.dds.k3003.documentos.DepositoDoc;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LogisticaDataMapper {

    public DepositoDTO toDepositoDTO(DepositoDoc doc) {
        if (doc == null) return null;

        List<PaqueteDTO> stock = doc.getPaquetesDeposito() != null
                ? doc.getPaquetesDeposito().stream()
                  .map(p -> new PaqueteDTO(
                          p.getPaqueteId(),
                          p.getDonacionID(),
                          p.getProductoID(),
                          p.getCantidad()
                  ))
                  .toList()
                : List.of();

        TipoAlgoritmoEnum algoritmoEnum = null;
        if (doc.getAlgoritmoMatchmaking() != null) {
            try {
                algoritmoEnum = TipoAlgoritmoEnum.valueOf(doc.getAlgoritmoMatchmaking());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return new DepositoDTO(
                doc.getId(), // String directo
                algoritmoEnum,
                doc.getNombre(),
                doc.getDireccion(),
                doc.getCapacidadMaxima(),
                stock
        );
    }



    public AsignacionDTO toAsignacionDTO(AsignacionDoc doc) {
        if (doc == null) return null;

        LocalDateTime fechaLocal = doc.fechaAsignacion() != null
                ? LocalDateTime.ofInstant(doc.fechaAsignacion(), java.time.ZoneId.systemDefault())
                : null;

        EstadoAsginacionEnum estadoEnum = null;
        if (doc.estado() != null) {
            try {
                estadoEnum = EstadoAsginacionEnum.valueOf(doc.estado());
            } catch (IllegalArgumentException ignored) {
            }
        }

        return new AsignacionDTO(
                doc.id(),
                doc.paqueteId(),
                doc.necesidadId(),
                fechaLocal,
                estadoEnum
        );
    }

    public PaqueteDTO toPaqueteDTO(PaqueteDoc doc) {
        if (doc == null) return null;

        return new PaqueteDTO(
                doc.getPaqueteId(), // Ojo, revisá si el DTO de cátedra espera String o Integer para el ID
                doc.getDonacionID(),
                doc.getProductoID(),
                doc.getCantidad()
        );
    }


}