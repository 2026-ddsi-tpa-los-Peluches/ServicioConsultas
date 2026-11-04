package ar.edu.utn.dds.k3003.documentos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "insignias")
public class InsigniaDoc {

    @Id
    private String id; // ID interno de Mongo (ObjectId)
    private Integer insigniaId; // ID original de PostgreSQL
    private String nombre;
    private String descripcion;

    public InsigniaDoc() {}

    public InsigniaDoc(Integer insigniaId, String nombre, String descripcion) {
        this.insigniaId = insigniaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}