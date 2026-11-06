package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.dtos.donaciones.*;
import ar.edu.utn.dds.k3003.documentos.ProductoDoc;
import ar.edu.utn.dds.k3003.repositories.Donaciones.ProductoMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceProducto {

    private final ProductoMongoRepository productoRepository;
    private final DonacionesDataMapper mapper;

    public ServiceProducto(ProductoMongoRepository productoRepository, DonacionesDataMapper mapper) {
        this.productoRepository = productoRepository;
        this.mapper = mapper;
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(mapper::toProductoDTO)
                .toList();
    }

    public ProductoDTO buscarPorId(String id) {
        ProductoDoc doc = productoRepository.findByProductoId(id)
                .or(() -> productoRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));
        return mapper.toProductoDTO(doc);
    }
}