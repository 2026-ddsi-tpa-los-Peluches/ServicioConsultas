package ar.edu.utn.dds.k3003.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDoc {

    @Id
    private String id; // ID interno de Mongo (ObjectId)
    private String productoId; // ID original de PostgreSQL (convertido a String)
    private String nombre;
    private String descripcion;

    // Categoría desnormalizada
    private String categoriaId;
    private String categoriaNombre;

    // Subcategoría desnormalizada
    private String subcategoriaId;
    private String subcategoriaNombre;

    // Identificador desnormalizado
    private String identificadorId;
    private String identificadorTipo;

    private Instant ultimaActualizacion;

    // Lista de stock por deposito
    private List<DepositoStockDoc> depositosStock = new ArrayList<>();

    @Data
    public static class DepositoStockDoc {
        private String depositoId;
        private String nombreDeposito; // Opcional, si querés mostrarlo lindo
        private Integer cantidadUnidades;
}


}