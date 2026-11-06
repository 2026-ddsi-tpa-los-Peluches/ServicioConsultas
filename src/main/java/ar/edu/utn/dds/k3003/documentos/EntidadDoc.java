package ar.edu.utn.dds.k3003.documentos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("entidades_beneficas")
public class EntidadDoc {
    @Id
    private String entidadId; // Guardado como String para mantener consistencia con los demás docs de Mongo

    private String razonSocial;
    private String domicilio;
    private String telefono;
    private String email;

    // Campos adicionales para consultas complejas o auditoría
    private Instant estadoActualizadoEn;
    private String estado; // Por si maneja algún estado operativo

    public EntidadDoc() {}

    public EntidadDoc(String entidadId, String razonSocial, String domicilio, String telefono, String email, Instant estadoActualizadoEn) {
        this.entidadId = entidadId;
        this.razonSocial = razonSocial;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.email = email;
        this.estadoActualizadoEn = estadoActualizadoEn;
    }
}