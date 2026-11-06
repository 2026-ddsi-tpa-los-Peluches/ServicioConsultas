package ar.edu.utn.dds.k3003.repositories.Logistica;

import ar.edu.utn.dds.k3003.documentos.DepositoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoMongoRepository extends MongoRepository<DepositoDoc, String> {
}