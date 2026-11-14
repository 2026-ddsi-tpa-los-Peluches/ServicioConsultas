package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.documentos.SubCategoriaDoc;
import ar.edu.utn.dds.k3003.dtos.donaciones.SubCategoriaDTO;
import ar.edu.utn.dds.k3003.repositories.Donaciones.SubCategoriaMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceSubCategoria {

    private final SubCategoriaMongoRepository subCategoriaRepository;

    public ServiceSubCategoria(SubCategoriaMongoRepository subCategoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
    }

    public List<SubCategoriaDTO> obtenerTodas() {
        return subCategoriaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public SubCategoriaDTO buscarPorId(String id) {
        Long subCategoriaId;
        try {
            subCategoriaId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new NoSuchElementException("ID de subcategoría inválido: " + id);
        }
        SubCategoriaDoc doc = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new NoSuchElementException("Subcategoría no encontrada con ID: " + id));
        return toDTO(doc);
    }

    private SubCategoriaDTO toDTO(SubCategoriaDoc doc) {
        return new SubCategoriaDTO(
                doc.getId() == null ? null : String.valueOf(doc.getId()),
                doc.getNombre(),
                doc.getDescripcion(),
                doc.getCategoriaId());
    }
}
