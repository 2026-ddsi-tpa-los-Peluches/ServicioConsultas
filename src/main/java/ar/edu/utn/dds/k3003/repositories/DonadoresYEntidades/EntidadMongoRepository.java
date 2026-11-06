package ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades;

import ar.edu.utn.dds.k3003.documentos.EntidadDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadMongoRepository extends MongoRepository<EntidadDoc, String> {
}