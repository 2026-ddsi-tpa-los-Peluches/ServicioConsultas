package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.documentos.IdentificadorDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.IdentificadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceIdentificador {

    private final IdentificadorMongoRepository identificadorRepository;
    private final DonacionesDataMapper mapper;

    public ServiceIdentificador(IdentificadorMongoRepository identificadorRepository, DonacionesDataMapper mapper) {
        this.identificadorRepository = identificadorRepository;
        this.mapper = mapper;
    }

    public List<IdentificadorDTO> obtenerTodos() {
        return identificadorRepository.findAll().stream()
                .map(mapper::toIdentificadorDTO)
                .toList();
    }

    public IdentificadorDTO buscarPorId(String id) {
        IdentificadorDoc doc = identificadorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Identificador no encontrado con ID: " + id));
        return mapper.toIdentificadorDTO(doc);
    }
}