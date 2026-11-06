package ar.edu.utn.dds.k3003.repositories.Donaciones;

import ar.edu.utn.dds.k3003.documentos.DonacionDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonacionMongoRepository extends MongoRepository<DonacionDoc, String> {
    Optional<DonacionDoc> findById(String id);

    // Para resolver la búsqueda por donador y fecha de ingreso
    List<DonacionDoc> findByDonadorIdAndFechaIngresoGreaterThanEqualOrderByFechaIngresoAsc(String donadorId, Instant fecha);
}