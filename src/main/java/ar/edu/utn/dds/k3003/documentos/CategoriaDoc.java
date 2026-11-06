package ar.edu.utn.dds.k3003.documentos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("categorias")
public class CategoriaDoc {
    @Id
    private String categoriaId;

    private String nombre;
    private String descripcion;
    private Instant actualizadoEn;

    public CategoriaDoc() {}

    public CategoriaDoc(String categoriaId, String nombre, String descripcion, Instant actualizadoEn) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.actualizadoEn = actualizadoEn;
    }
}