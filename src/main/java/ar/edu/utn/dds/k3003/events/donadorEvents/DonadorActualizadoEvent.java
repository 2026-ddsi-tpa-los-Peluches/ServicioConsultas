package ar.edu.utn.dds.k3003.events.donadorEvents;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@Document(collection = "donadores")
public class DonadorActualizadoEvent {

    @Id
    private String id; // El ID del donador (puede ser String o Integer según cómo lo manejen)

    // Datos personales (vienen de donadoresYEntidades)
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String nroDocumento;
    private String domicilio;
    private String estado;
    private List<String> historial = new ArrayList<>();

    // Datos de incentivos (vienen del microservicio de incentivos)
    private String categoriaActual;
    private List<String> historialCategorias = new ArrayList<>();
    private List<String> insignias = new ArrayList<>();
    private String misionActual;

    // Control de concurrencia/fechas para tu actualizador
    private Instant estadoActualizadoEn;
    private Instant incentivosActualizadoEn;
    private Instant ultimaActualizacion;

    public DonadorActualizadoEvent() {}
}