package ar.edu.utn.dds.k3003.documentos;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Document("depositos")
public class DepositoDoc {
    @Id
    String id;

    //Datos generales del Deposito
    String nombre;
    String direccion;
    String algoritmoMatchmaking;
    Integer capacidadMaxima;


    // cantidad de stock del deposito en unidades generales
    Integer stockTotal;


    //cantidad de asignaciones por estado
    List<AsignacionEstadoDoc> cantidadAsignacionesPorEstado;


    //cantidad de entidades distintas atendidas
    Integer cantidadDeEnttidadesDistintasAtendidas;
    List<EntidadesAtendidasDoc> entidadesAtendidas;


    Instant ultimaActualizacion;

    public DepositoDoc() {
    }


    @Data
    public static class AsignacionEstadoDoc {
        private String estadoAsignacion;
        private String cantidad;
        private LocalDate fecha;
    }

    @Data
    public static class EntidadesAtendidasDoc{
        private Integer entidadId;
        private String razonSocail;
        private Integer unidadesEntregadas;
    }
}