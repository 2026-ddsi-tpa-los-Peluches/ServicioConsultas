package ar.edu.utn.dds.k3003.events.donadorEvents;

import lombok.Getter;

import java.time.Instant;


@Getter
public class DonadorCreadoEvent {
    private Integer id;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String nroDocumento;
    private String domicilio;
    private String estado;
    private String categoria;
    private Instant fechaEvento;

    public DonadorCreadoEvent(Integer id, String nombre, String apellido, Integer edad, 
                              String email, String nroDocumento, String domicilio, 
                              String estado, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.nroDocumento = nroDocumento;
        this.domicilio = domicilio;
        this.estado = estado;
        this.categoria = categoria;
        this.fechaEvento = Instant.now();
    }
}