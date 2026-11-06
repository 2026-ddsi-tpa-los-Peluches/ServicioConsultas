package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.documentos.IdentificadorDoc;
import ar.edu.utn.dds.k3003.documentos.ProductoDoc;
import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.documentos.DonacionDoc;
import org.springframework.stereotype.Component;

@Component
public class DonacionesDataMapper {

    public DonacionDTO toDonacionDTO(DonacionDoc doc) {
        if (doc == null) return null;

        return new DonacionDTO(
                doc.id(),                               // 1. id
                doc.donadorId(),                        // 2. donadorID
                doc.depositoId(),                       // 3. depositoID (¡ahora sí!)
                doc.queja() != null ? doc.queja().descripcion() : null, // 4. descripcion (o la queja si aplica)
                doc.productoId(),                       // 5. productoID
                doc.cantidad(),                         // 6. cantidad
                doc.estado() != null ? EstadoDonacionEnum.valueOf(doc.estado()) : null // 7. estado
        );
    }


    public ProductoDTO toProductoDTO(ProductoDoc doc) {
        if (doc == null) return null;
        return new ProductoDTO(
                doc.getProductoId() != null ? doc.getProductoId() : doc.getId(),
                doc.getNombre(),
                doc.getDescripcion(),
                doc.getCategoriaId(),
                doc.getIdentificadorId()
        );
    }

    public IdentificadorDTO toIdentificadorDTO(IdentificadorDoc doc) {
        if (doc == null) return null;
        return new IdentificadorDTO(
                doc.getIdentificadorId(),
                doc.getTipo(),
                doc.getDescripcion()
        );
    }

}