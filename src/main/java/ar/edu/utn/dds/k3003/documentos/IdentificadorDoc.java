package ar.edu.utn.dds.k3003.documentos;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("identificadores")
public class IdentificadorDoc {
    @Id
    private String identificadorId;

    private String descripcion;
    
    private TipoIdentificadorEnum tipo;
    
    private Instant actualizadoEn;

    public IdentificadorDoc() {}

    public IdentificadorDoc(String identificadorId, String descripcion, TipoIdentificadorEnum tipo, Instant actualizadoEn) {
        this.identificadorId = identificadorId;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.actualizadoEn = actualizadoEn;
    }
}