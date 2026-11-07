package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.SubCategoriaDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubCategoriaMongoRepository extends MongoRepository<SubCategoriaDoc, String> {
    Optional<SubCategoriaDoc> findById(String id);
}