package ar.edu.utn.dds.k3003.repositories.DonadoresYEntidades;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonadorMongoRepository extends MongoRepository<DonadorDoc, String> {
    // Spring te provee los métodos básicos automáticamente (findAll, save, etc.)
    Optional<DonadorDoc> findByDonadorId(String donadorId);
}