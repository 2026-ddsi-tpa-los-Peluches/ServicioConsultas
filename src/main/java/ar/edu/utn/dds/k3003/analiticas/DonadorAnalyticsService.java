package ar.edu.utn.dds.k3003.analiticas;

import ar.edu.utn.dds.k3003.documentos.DonadorDoc;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonadorAnalyticsService {

    private final MongoTemplate mongoTemplate;

    public DonadorAnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public DonadorDoc obtenerReporteDonador(String donadorId) {

        // Armamos un pipeline de agregación sencillo con $match por ID del donador
        List<Document> pipeline = List.of(
                Document.parse("{ $match: { _id: '" + donadorId + "' } }")
        );

        // Ejecutamos el aggregate crudo sobre la colección "donadores"
        Document resultadoCrudo = mongoTemplate.getCollection("donadores")
                .aggregate(pipeline)
                .first();

        if (resultadoCrudo == null) {
            return null;
        }

        // Mapeamos el Document de MongoDB directamente a tu entidad DonadorDoc
        return mongoTemplate.getConverter().read(DonadorDoc.class, resultadoCrudo);
    }
}