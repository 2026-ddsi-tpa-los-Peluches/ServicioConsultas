package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.logistica.DepositoDTO;
import ar.edu.utn.dds.k3003.documentos.DepositoDoc;
import ar.edu.utn.dds.k3003.repositories.Logistica.DepositoMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceDeposito {

    private final DepositoMongoRepository depositoRepository;
    private final LogisticaDataMapper mapper;

    public ServiceDeposito(DepositoMongoRepository depositoRepository, LogisticaDataMapper mapper) {
        this.depositoRepository = depositoRepository;
        this.mapper = mapper;
    }

    public List<DepositoDTO> obtenerTodos() {
        return depositoRepository.findAll().stream()
                .map(mapper::toDepositoDTO)
                .toList();
    }

    public DepositoDTO buscarPorId(String id) {
        DepositoDoc doc = depositoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Depósito no encontrado con ID: " + id));
        return mapper.toDepositoDTO(doc);
    }
}