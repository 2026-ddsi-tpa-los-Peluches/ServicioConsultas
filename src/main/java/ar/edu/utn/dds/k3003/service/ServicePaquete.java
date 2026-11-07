package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.logistica.PaqueteDTO;
import ar.edu.utn.dds.k3003.documentos.PaqueteDoc;
import ar.edu.utn.dds.k3003.repositories.Logistica.PaqueteMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicePaquete {

    private final PaqueteMongoRepository paqueteRepository;
    private final LogisticaDataMapper mapper;

    public ServicePaquete(PaqueteMongoRepository paqueteRepository, LogisticaDataMapper mapper) {
        this.paqueteRepository = paqueteRepository;
        this.mapper = mapper;
    }

    public List<PaqueteDTO> obtenerTodos() {
        return paqueteRepository.findAll().stream()
                .map(mapper::toPaqueteDTO)
                .toList();
    }

    public PaqueteDTO buscarPorId(String id) {
        PaqueteDoc doc = paqueteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Paquete no encontrado con ID: " + id));
        return mapper.toPaqueteDTO(doc);
    }
}