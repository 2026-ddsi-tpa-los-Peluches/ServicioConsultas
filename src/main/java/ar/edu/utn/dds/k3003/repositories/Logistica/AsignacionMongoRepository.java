package ar.edu.utn.dds.k3003.repositories.Logistica;

import ar.edu.utn.dds.k3003.documentos.AsignacionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionMongoRepository extends MongoRepository<AsignacionDoc, String> {
}