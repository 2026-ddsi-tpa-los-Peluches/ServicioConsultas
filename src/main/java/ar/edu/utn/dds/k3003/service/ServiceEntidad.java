package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.documentos.EntidadDoc;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades.EntidadMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceEntidad {

    private final EntidadMongoRepository entidadRepository;
    private final DonadoresYEntidadesDataMapper mapper;

    public ServiceEntidad(EntidadMongoRepository entidadRepository, DonadoresYEntidadesDataMapper mapper) {
        this.entidadRepository = entidadRepository;
        this.mapper = mapper;
    }

    public List<EntidadBeneficaDTO> obtenerTodas() {
        return entidadRepository.findAll().stream()
                .map(mapper::toEntidadBeneficaDTO)
                .toList();
    }

    public EntidadBeneficaDTO buscarPorId(Integer id) {
        EntidadDoc doc = entidadRepository.findById(id.toString())
                .orElseThrow(() -> new NoSuchElementException("Entidad benéfica no encontrada con ID: " + id));
        return mapper.toEntidadBeneficaDTO(doc);
    }
}