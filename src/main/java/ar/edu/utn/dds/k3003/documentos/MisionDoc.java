package ar.edu.utn.dds.k3003.documentos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "misiones")
public class MisionDoc {

    @Id
    private Integer misionId;
    private String nombre;
    private String insigniaId;
    private String categoriaInicio;
    private String categoriaFin;
    private String tipoMision;

    public MisionDoc() {}

    public MisionDoc(Integer misionId, String nombre, String insigniaId, String categoriaInicio, String categoriaFin, String tipoMision) {
        this.misionId = misionId;
        this.nombre = nombre;
        this.insigniaId = insigniaId;
        this.categoriaInicio = categoriaInicio;
        this.categoriaFin = categoriaFin;
        this.tipoMision = tipoMision;
    }
}