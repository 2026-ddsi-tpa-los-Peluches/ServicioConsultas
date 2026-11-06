package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.documentos.NecesidadDoc;
import ar.edu.utn.dds.k3003.repositories.NecesidadMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceNecesidad {

    private final NecesidadMongoRepository necesidadRepository;
    private final DonadoresYEntidadesDataMapper mapper;

    public ServiceNecesidad(NecesidadMongoRepository necesidadRepository, DonadoresYEntidadesDataMapper mapper) {
        this.necesidadRepository = necesidadRepository;
        this.mapper = mapper;
    }

    public NecesidadMaterialDTO buscarPorId(Integer id) {
        NecesidadDoc doc = necesidadRepository.findById(id.toString())
                .orElseThrow(() -> new NoSuchElementException("No existe una necesidad con ID: " + id));
        return mapper.toNecesidadMaterialDTO(doc);
    }

    public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoId) {
        // Traemos todas las del producto y filtramos las que todavía no están completas
        return necesidadRepository.findAll().stream()
                .filter(n -> n.productoId().equals(productoId) && n.cantidadRecibida() < n.cantidadObjetivo())
                .map(mapper::toNecesidadMaterialDTO)
                .toList();
    }
}