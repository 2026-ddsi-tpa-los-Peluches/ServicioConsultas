package ar.edu.utn.dds.k3003.documentos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("paquetes")
public class PaqueteDoc {
    @Id
    private String paqueteId; // Guardado como String para unificar identificadores en Mongo

    private String donacionID;
    private String productoID;
    private Integer cantidad;
    private Integer depositoID;
    private Instant actualizadoEn;

    public PaqueteDoc() {}

    public PaqueteDoc(String paqueteId, String donacionID, String productoID, Integer cantidad, Integer depositoID, Instant actualizadoEn) {
        this.paqueteId = paqueteId;
        this.donacionID = donacionID;
        this.productoID = productoID;
        this.cantidad = cantidad;
        this.depositoID = depositoID;
        this.actualizadoEn = actualizadoEn;
    }
}