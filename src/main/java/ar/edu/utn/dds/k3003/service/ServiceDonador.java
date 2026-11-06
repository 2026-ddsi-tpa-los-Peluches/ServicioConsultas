package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades.DonadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceDonador {
    private final DonadorMongoRepository donadorRepository;
    private final DonadoresYEntidadesDataMapper mapper;

    public ServiceDonador(DonadorMongoRepository donadorRepository, DonadoresYEntidadesDataMapper mapper) {
        this.donadorRepository = donadorRepository;
        this.mapper = mapper;
    }

    public List<DonadorDTO> obtenerTodos() {
        return donadorRepository.findAll().stream()
                .map(mapper::toDonadorDTO)
                .toList();
    }

    public DonadorDTO obtenerPorId(Integer id) {
        DonadorDoc doc = donadorRepository.findByDonadorId(id.toString())
                .orElseThrow(() -> new NoSuchElementException("Donador no encontrado con ID: " + id));
        return mapper.toDonadorDTO(doc);
    }

    public Boolean puedeDonar(Integer id) {
        DonadorDoc doc = donadorRepository.findByDonadorId(id.toString())
                .orElseThrow(() -> new NoSuchElementException("Donador no encontrado con ID: " + id));
        return "VERIFICADO".equalsIgnoreCase(doc.getEstado());
    }

    public DonadorStatsDTO estadisticasDonador(Integer id) {
        DonadorDoc doc = donadorRepository.findByDonadorId(id.toString())
                .orElseThrow(() -> new NoSuchElementException("Donador no encontrado con ID: " + id));
        return mapper.toDonadorStatsDTO(doc);
    }

    // Método para obtener las quejas del donador desde el documento de Mongo
    public List<QuejaDTO> obtenerQuejasDe(Integer id) {
        DonadorDoc doc = donadorRepository.findByDonadorId(id.toString())
                .orElseThrow(() -> new NoSuchElementException("Donador no encontrado con ID: " + id));
        return mapper.toQuejaDTOList(doc.getQuejas(), id.toString());
    }
}

