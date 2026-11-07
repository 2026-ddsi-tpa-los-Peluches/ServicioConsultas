package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.CategoriaDoc;
import ar.edu.utn.dds.k3003.documentos.DonacionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaMongoRepository extends MongoRepository<CategoriaDoc, String> {
    Optional<CategoriaDoc> findById(String id);
}
