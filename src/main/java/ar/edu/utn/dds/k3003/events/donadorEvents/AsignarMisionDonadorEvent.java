package ar.edu.utn.dds.k3003.events.donadorEvents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignarMisionDonadorEvent {
    private String donadorId;
    private String misionId;
    private String nombre;
    private String insigniaId;
    private String tipoMision;
    private Integer progreso;
    private Integer objetivo;
    private String categoriaInicio;
    private String categoriaFin;
}