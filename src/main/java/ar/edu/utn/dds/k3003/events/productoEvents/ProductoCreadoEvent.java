package ar.edu.utn.dds.k3003.events.productoEvents;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ProductoCreadoEvent {
    private String productoId;
    private String nombre;
    private String descripcion;
    
    private String categoriaId;
    private String categoriaNombre;
    
    private String subcategoriaId;
    private String subcategoriaNombre;
    
    private String identificadorId;
    private String identificadorTipo;
    
    private Instant fechaEvento;

    public ProductoCreadoEvent(String productoId, String nombre, String descripcion, 
                               String categoriaId, String categoriaNombre, 
                               String subcategoriaId, String subcategoriaNombre, 
                               String identificadorId, String identificadorTipo ) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.subcategoriaId = subcategoriaId;
        this.subcategoriaNombre = subcategoriaNombre;
        this.identificadorId = identificadorId;
        this.identificadorTipo = identificadorTipo;
        this.fechaEvento = Instant.now();
    }
}