package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.documentos.InsigniaDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsigniaMongoRepository extends MongoRepository<InsigniaDoc, String> {
    Optional<InsigniaDoc> findByInsigniaId(Integer insigniaId);
}