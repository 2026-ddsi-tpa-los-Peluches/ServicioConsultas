package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.documentos.NecesidadDoc;
import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DonadoresYEntidadesDataMapper {

    public DonadorDTO toDonadorDTO(DonadorDoc doc) {
        if (doc == null) return null;

        return new DonadorDTO(
                doc.getDonadorId() != null ? Integer.valueOf(doc.getDonadorId()) : null,
                doc.getNombre(),
                doc.getApellido(),
                doc.getEdad(),
                doc.getEmail(),
                doc.getNroDocumento(),
                doc.getDomicilio(),
                doc.getEstado() != null ? EstadoDonadorEnum.valueOf(doc.getEstado()) : null,
                doc.getCategoriaActual()
        );
    }

    public DonadorStatsDTO toDonadorStatsDTO(DonadorDoc doc) {
        if (doc == null) return null;

        return new DonadorStatsDTO(
                doc.getDonadorId() != null ? Integer.valueOf(doc.getDonadorId()) : null,
                doc.getNombre(),
                doc.getApellido(),
                doc.getEdad(),
                doc.getEstado() != null ? EstadoDonadorEnum.valueOf(doc.getEstado()) : null,
                doc.getCategoriaActual(),
                doc.getMisionActual() != null ? doc.getMisionActual().getMisionId() : null,
                doc.getInsignias()
        );
    }

    // Mapeo de quejas sacado directamente del documento de Mongo
    public List<QuejaDTO> toQuejaDTOList(List<DonadorDoc.QuejaDoc> quejasDoc, String donadorId) {
        if (quejasDoc == null) return List.of();

        return quejasDoc.stream().map(q -> new QuejaDTO(
                q.getQuejaId() != null ? Integer.valueOf(q.getQuejaId().toString()) : null,
                q.getDonacionId(),
                donadorId,
                q.getFecha(),
                q.getMotivo()
        )).toList();
    }


    public NecesidadMaterialDTO toNecesidadMaterialDTO(NecesidadDoc doc) {
        if (doc == null) return null;

        return new NecesidadMaterialDTO(
                doc.id() != null ? Integer.valueOf(doc.id()) : null,
                doc.entidadId(),
                doc.nivelUrgencia(),
                doc.descripcion(),
                doc.cantidadObjetivo(),
                doc.cantidadRecibida(),
                doc.productoId(),
                doc.tipoNecesidad() != null ? TipoNecesidadMaterialEnum.valueOf(doc.tipoNecesidad()) : null
        );
    }
}