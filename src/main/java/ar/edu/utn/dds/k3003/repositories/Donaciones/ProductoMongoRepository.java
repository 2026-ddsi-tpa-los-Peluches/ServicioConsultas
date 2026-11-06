package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.ProductoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoMongoRepository extends MongoRepository<ProductoDoc, String> {
    Optional<ProductoDoc> findByProductoId(String productoId);
}