package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.documentos.NecesidadDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NecesidadMongoRepository extends MongoRepository<NecesidadDoc, String> {
    Optional<NecesidadDoc> findById(String id);

    // Para buscar necesidades insatisfechas de un producto específico
    List<NecesidadDoc> findByProductoIdAndCantidadRecibidaLessThan(String productoId, int cantidadObjetivo);
}