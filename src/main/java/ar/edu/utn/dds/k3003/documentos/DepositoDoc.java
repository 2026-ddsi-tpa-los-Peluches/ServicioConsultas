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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, Long> cantidadAsignacionesPorEstado = new HashMap<>();


    //cantidad de entidades distintas atendidas
    Integer cantidadDeEntidadesDistintasAtendidas;
    List<EntidadesAtendidasDoc> entidadesAtendidas;


    //lista de paquetes que estan en el deposito
    private List<PaqueteStockDoc> paquetesDeposito = new ArrayList<>();


    //lista de donaciones que entraron al deposito
    private List<DonacionDepositoDoc> donacionesDeposito = new ArrayList<>();




    Instant ultimaActualizacion;

    public DepositoDoc() {
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntidadesAtendidasDoc {
        private String entidadId;
        private String razonSocial;
        private Integer unidadesEntregadas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonacionDepositoDoc {
        private String donacionId;
        private String donadorId;
        private int cantidadDonada;
        private Instant fechaQueLlego;
        private Instant fechaQueSeEntrego; // Nulo hasta que se entrega
    }
}