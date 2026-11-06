package ar.edu.utn.dds.k3003.repositories.Incentivos;

import ar.edu.utn.dds.k3003.documentos.MisionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MisionMongoRepository extends MongoRepository<MisionDoc, Integer> {
    Optional<MisionDoc> findByMisionId(Integer misionId);
}