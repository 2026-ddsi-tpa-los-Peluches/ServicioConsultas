package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.service.DonacionesDataMapper;
import ar.edu.utn.dds.k3003.documentos.CategoriaDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.CategoriaMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceCategoria {

    private final CategoriaMongoRepository categoriaRepository;
    private final DonacionesDataMapper mapper;

    public ServiceCategoria(CategoriaMongoRepository categoriaRepository, DonacionesDataMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(mapper::toCategoriaDTO)
                .toList();
    }

    public CategoriaDTO buscarPorId(String id) {
        CategoriaDoc doc = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
        return mapper.toCategoriaDTO(doc);
    }
}