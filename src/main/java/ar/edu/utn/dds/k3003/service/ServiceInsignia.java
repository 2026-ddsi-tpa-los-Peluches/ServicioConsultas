package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.incentivos.*;
import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import ar.edu.utn.dds.k3003.documentos.InsigniaDoc;
import ar.edu.utn.dds.k3003.repositories.InsigniaMongoRepository;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades.DonadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceInsignia {

    private final InsigniaMongoRepository insigniaRepository;
    private final DonadorMongoRepository donadorRepository;
    private final IncentivosDataMapper mapper;

    public ServiceInsignia(InsigniaMongoRepository insigniaRepository,
                           DonadorMongoRepository donadorRepository,
                           IncentivosDataMapper mapper) {
        this.insigniaRepository = insigniaRepository;
        this.donadorRepository = donadorRepository;
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

    public List<InsigniaDTO> obtenerPorDonador(String donadorId) {
        DonadorDoc donador = donadorRepository.findById(donadorId)
                .orElseThrow(() -> new NoSuchElementException("Donador no encontrado con ID: " + donadorId));

        List<InsigniaDTO> insignias = new ArrayList<>();
        for (Object insigniaId : donador.getInsignias()) {
            try {
                insignias.add(buscarPorId(String.valueOf(insigniaId)));
            } catch (NoSuchElementException e) {
                // insignia que ya no existe en la colección: se omite
            }
        }
        return insignias;
    }
}