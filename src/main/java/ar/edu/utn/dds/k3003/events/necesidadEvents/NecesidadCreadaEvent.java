package ar.edu.utn.dds.k3003.events.necesidadEvents;

import lombok.Getter;

import java.time.Instant;

@Getter
public class NecesidadCreadaEvent {
    private String id;
    private String entidadId;
    private String razonSocial;
    private int nivelUrgencia;
    private String descripcion;
    private String tipoNecesidad;
    private String productoId;
    private String nombreProducto;
    private String categoriaProducto;
    private String subcategoriaProducto;
    private int cantidadObjetivo;
    private Instant fechaEvento;

    public NecesidadCreadaEvent(String id, String entidadId, String razonSocial, int nivelUrgencia, 
                                String descripcion, String tipoNecesidad, String productoId, 
                                String nombreProducto, String categoriaProducto, String subcategoriaProducto, 
                                int cantidadObjetivo) {
        this.id = id;
        this.entidadId = entidadId;
        this.razonSocial = razonSocial;
        this.nivelUrgencia = nivelUrgencia;
        this.descripcion = descripcion;
        this.tipoNecesidad = tipoNecesidad;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.categoriaProducto = categoriaProducto;
        this.subcategoriaProducto = subcategoriaProducto;
        this.cantidadObjetivo = cantidadObjetivo;
        this.fechaEvento = Instant.now();
    }
}