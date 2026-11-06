package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.documentos.MisionDoc;
import ar.edu.utn.dds.k3003.repositories.Incentivos.MisionMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceMision {

    private final MisionMongoRepository misionRepository;
    private final IncentivosDataMapper mapper;

    public ServiceMision(MisionMongoRepository misionRepository, IncentivosDataMapper mapper) {
        this.misionRepository = misionRepository;
        this.mapper = mapper;
    }

    public List<MisionDTO> obtenerTodas() {
        return misionRepository.findAll().stream()
                .map(mapper::toMisionDTO)
                .toList();
    }

    public MisionDTO buscarPorId(String id) {
        Integer misionId;
        try {
            misionId = Integer.valueOf(id);
        } catch (NumberFormatException e) {
            throw new NoSuchElementException("ID de misión inválido: " + id);
        }
        MisionDoc doc = misionRepository.findByMisionId(misionId)
                .orElseThrow(() -> new NoSuchElementException("Misión no encontrada con ID: " + id));
        return mapper.toMisionDTO(doc);
    }
}