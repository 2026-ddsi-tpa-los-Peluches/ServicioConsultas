package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.documentos.InsigniaDoc;
import ar.edu.utn.dds.k3003.repositories.InsigniaMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceInsignia {

    private final InsigniaMongoRepository insigniaRepository;
    private final IncentivosDataMapper mapper;

    public ServiceInsignia(InsigniaMongoRepository insigniaRepository, IncentivosDataMapper mapper) {
        this.insigniaRepository = insigniaRepository;
        this.mapper = mapper;
    }

    public List<InsigniaDTO> obtenerTodas() {
        return insigniaRepository.findAll().stream()
                .map(mapper::toInsigniaDTO)
                .toList();
    }

    public InsigniaDTO buscarPorId(String id) {
        InsigniaDoc doc = insigniaRepository.findById(id)
                .or(() -> {
                    try {
                        return insigniaRepository.findByInsigniaId(Integer.valueOf(id));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                })
                .orElseThrow(() -> new NoSuchElementException("Insignia no encontrada con ID: " + id));
        return mapper.toInsigniaDTO(doc);
    }
}