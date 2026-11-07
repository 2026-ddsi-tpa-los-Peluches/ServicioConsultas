package ar.edu.utn.dds.k3003.repositories.Logistica;

import ar.edu.utn.dds.k3003.documentos.PaqueteDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteMongoRepository extends MongoRepository<PaqueteDoc, String> {
}