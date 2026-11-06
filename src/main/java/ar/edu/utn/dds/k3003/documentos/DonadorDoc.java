package ar.edu.utn.dds.k3003.documentos;

import ar.edu.utn.dds.k3003.dtos.CategoriaHistoricaDTO;
import ar.edu.utn.dds.k3003.dtos.MisionHistoricaDTO;
import ar.edu.utn.dds.k3003.model.MisionActual;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document("donadores")
public class DonadorDoc {
  @Id
  String donadorId;

  // Datos Personales
  String nombre;
  String apellido;
  Integer edad;
  String email;
  String nroDocumento;
  String domicilio;

  // Estado de un donador
  String estado;

//        Instant estadoActualizadoEn,

  // Categoria de un donador
  String categoriaActual;
  List<CategoriaHistoricaDoc> historialCategorias;


  // Mision de un Donador
  MisionActualDonadorDoc misionActual;
  List<MisionHistoricaDonadorDoc> misionesCompletadasHistorial = new ArrayList<>();


  // Insignias de un Donador
  List<String> insignias = new ArrayList<>();

  // Cantidad de donaciones por cada estado de donacion
  Map<String, Long> cantidadesPorEstado = new HashMap<>();

  // lista de quejas de un donador
  private List<QuejaDoc> quejas = new ArrayList<>();


  // lista de las entidades distintas donadas por un donador con la cantidad donada
  private List<EntidadBeneficiadaDoc> entidadesBeneficiadas = new ArrayList<>();





  public DonadorDoc() {}


  @Data
  public static class MisionActualDonadorDoc {
    private String misionId;
    private String nombre;
    private String insigniaId;
    private String tipoMision;
    private Integer progreso;
    private Integer objetivo;
    private String categoriaInicio;
    private String categoriaFin;

    public MisionActualDonadorDoc() {}

    public MisionActualDonadorDoc(String misionId, String nombre, String insigniaId, String tipoMision,
                                  Integer progreso, Integer objetivo, String categoriaInicio, String categoriaFin) {
      this.misionId = misionId;
      this.nombre = nombre;
      this.insigniaId = insigniaId;
      this.tipoMision = tipoMision;
      this.progreso = progreso;
      this.objetivo = objetivo;
      this.categoriaInicio = categoriaInicio;
      this.categoriaFin = categoriaFin;
    }
  }


  @Data
  public static class QuejaDoc {
    private Integer quejaId;
    private String donacionId;
    private String motivo;
    private LocalDate fecha;
  }

  @Data
  public static class EntidadBeneficiadaDoc {
    private String entidadId;
    private String razonSocialEntidad;
    private Integer unidadesEntregadas;
  }

  @Data
  public static class MisionHistoricaDonadorDoc {
    String misionId;
    String nombre;
    LocalDateTime completadaEn;
  }

  @Data
  public static class CategoriaHistoricaDoc {
    String categoria;
    Instant fecha;
  }

}
