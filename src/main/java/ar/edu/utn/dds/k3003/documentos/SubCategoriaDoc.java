package ar.edu.utn.dds.k3003.documentos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "subcategorias")
public class SubCategoriaDoc {

    @Id
    private Long id; // Coincide con el Long de Postgres
    private String nombre;
    private String descripcion;
    private String categoriaId; // Almacenamos el ID de la categoría asociada

    public SubCategoriaDoc() {}

    public SubCategoriaDoc(Long id, String nombre, String descripcion, String categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
    }
}