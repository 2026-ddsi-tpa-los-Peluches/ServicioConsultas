package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.documentos.InsigniaDoc;
import ar.edu.utn.dds.k3003.documentos.MisionDoc;
import org.springframework.stereotype.Component;

@Component
public class IncentivosDataMapper {

    public InsigniaDTO toInsigniaDTO(InsigniaDoc doc) {
        if (doc == null) return null;
        String idStr = doc.getInsigniaId().toString();
        return new InsigniaDTO(idStr, doc.getNombre(), doc.getDescripcion());
    }

    public MisionDTO toMisionDTO(MisionDoc doc) {
        if (doc == null) return null;
        return new MisionDTO(
                doc.getMisionId() != null ? doc.getMisionId().toString() : null,
                doc.getNombre(),
                doc.getInsigniaId(),
                doc.getCategoriaInicio() != null ? CategoriaDonadorEnum.valueOf(doc.getCategoriaInicio()) : null,
                doc.getCategoriaFin() != null ? CategoriaDonadorEnum.valueOf(doc.getCategoriaFin()) : null,
                doc.getTipoMision() != null ? TipoMisionEnum.valueOf(doc.getTipoMision()) : null
        );
    }
}