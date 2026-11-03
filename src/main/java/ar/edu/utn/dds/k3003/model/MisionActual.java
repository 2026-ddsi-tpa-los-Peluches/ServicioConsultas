package ar.edu.utn.dds.k3003.model;

import java.time.Instant;

public class MisionActual {

    private String misionId;
    private String nombre;
    private String insigniaId;
    private String tipoMision;
    private Integer progreso;
    private Integer objetivo;
    private String categoriaInicio;
    private String categoriaFin;

    // Constructor vacío requerido por Spring Data / Jackson
    public MisionActual() {}

    // Constructor completo
    public MisionActual(String misionId, String nombre, String insigniaId, String tipoMision, 
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

    // Getters y Setters
    public String getMisionId() {
        return misionId;
    }

    public void setMisionId(String misionId) {
        this.misionId = misionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInsigniaId() {
        return insigniaId;
    }

    public void setInsigniaId(String insigniaId) {
        this.insigniaId = insigniaId;
    }

    public String getTipoMision() {
        return tipoMision;
    }

    public void setTipoMision(String tipoMision) {
        this.tipoMision = tipoMision;
    }

    public Integer getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progreso) {
        this.progreso = progreso;
    }

    public Integer getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Integer objetivo) {
        this.objetivo = objetivo;
    }

    public String getCategoriaInicio() {
        return categoriaInicio;
    }

    public void setCategoriaInicio(String categoriaInicio) {
        this.categoriaInicio = categoriaInicio;
    }

    public String getCategoriaFin() {
        return categoriaFin;
    }

    public void setCategoriaFin(String categoriaFin) {
        this.categoriaFin = categoriaFin;
    }
}