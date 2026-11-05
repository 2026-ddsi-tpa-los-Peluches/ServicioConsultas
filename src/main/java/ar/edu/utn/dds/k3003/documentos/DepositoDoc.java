package ar.edu.utn.dds.k3003.documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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


    //lista de paquetes que estan en el deposito
    private List<PaqueteStockDoc> stockActual = new ArrayList<>();


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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaqueteStockDoc {
        private String paqueteId;
        private String donacionID;
        private String productoID;
        private int cantidad;
    }
}