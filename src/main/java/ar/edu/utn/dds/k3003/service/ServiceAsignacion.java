package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.logistica.AsignacionDTO;
import ar.edu.utn.dds.k3003.documentos.AsignacionDoc;
import ar.edu.utn.dds.k3003.repositories.Logistica.AsignacionMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceAsignacion {

    private final AsignacionMongoRepository asignacionRepository;
    private final LogisticaDataMapper mapper;

    public ServiceAsignacion(AsignacionMongoRepository asignacionRepository, LogisticaDataMapper mapper) {
        this.asignacionRepository = asignacionRepository;
        this.mapper = mapper;
    }

    public List<AsignacionDTO> obtenerTodas() {
        return asignacionRepository.findAll().stream()
                .map(mapper::toAsignacionDTO)
                .toList();
    }

    public AsignacionDTO buscarPorId(String id) {
        AsignacionDoc doc = asignacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asignación no encontrada con ID: " + id));
        return mapper.toAsignacionDTO(doc);
    }
}