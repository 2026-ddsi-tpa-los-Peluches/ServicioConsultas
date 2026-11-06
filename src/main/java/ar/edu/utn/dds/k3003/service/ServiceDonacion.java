package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.documentos.DonacionDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.DonacionMongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceDonacion {

    private final DonacionMongoRepository donacionRepository;
    private final DonacionesDataMapper mapper;

    public ServiceDonacion(DonacionMongoRepository donacionRepository, DonacionesDataMapper mapper) {
        this.donacionRepository = donacionRepository;
        this.mapper = mapper;
    }

    public List<DonacionDTO> obtenerTodas() {
        return donacionRepository.findAll().stream()
                .map(mapper::toDonacionDTO)
                .toList();
    }

    public DonacionDTO buscarPorId(String id) {
        DonacionDoc doc = donacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Donación no encontrada con ID: " + id));
        return mapper.toDonacionDTO(doc);
    }

    public List<DonacionDTO> buscarPorDonadorYFecha(String donadorId, LocalDate fechaInicio) {
        java.time.Instant instantInicio = fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant();
        List<DonacionDoc> docs = donacionRepository.findByDonadorIdAndFechaIngresoGreaterThanEqualOrderByFechaIngresoAsc(donadorId, instantInicio);
        return docs.stream().map(mapper::toDonacionDTO).toList();
    }
}