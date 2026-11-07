package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.CategoriaDoc;
import ar.edu.utn.dds.k3003.documentos.DonacionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoriaMongoRepository extends MongoRepository<CategoriaDoc, String> {
    Optional<CategoriaDoc> findById(String id);
}