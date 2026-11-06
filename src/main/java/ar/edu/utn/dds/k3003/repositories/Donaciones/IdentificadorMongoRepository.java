package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.IdentificadorDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificadorMongoRepository extends MongoRepository<IdentificadorDoc, String> {
}