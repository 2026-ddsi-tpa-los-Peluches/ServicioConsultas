package ar.edu.utn.dds.k3003.documentos;

import ar.edu.utn.dds.k3003.dtos.CategoriaHistoricaDTO;
import ar.edu.utn.dds.k3003.dtos.MisionHistoricaDTO;
import ar.edu.utn.dds.k3003.model.MisionActual;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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
  List<CategoriaHistoricaDTO> historialCategorias;


  // Mision de un Donador
  MisionActual misionActual;
  List<MisionHistoricaDTO> misionesCompletadasHistorial = new ArrayList<>();



  public DonadorDoc() {}

}


//        List<Insignia> insignias,
//        Instant incentivosActualizadoEn,
//        Map<String, Integer> donacionesPorEstado,
//        int quejasRecibidas,
//        List<EntidadBeneficiada> entidades,
//        Instant resumenActualizadoEn
// ) {
////  public record Insignia(String insigniaId, String nombre) {}
////  public record MisionActual(String misionId, String nombre, int progreso, int objetivo) {}
////  public record EntidadBeneficiada(String entidadId, String razonSocial, int unidades) {}
//}