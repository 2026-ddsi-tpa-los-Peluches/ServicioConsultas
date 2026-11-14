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

        // Armamos el pipeline de agregación: $match por ID del donador (con Document, no interpolado)
        // y recalculamos cantidadesPorEstado desde la colección "donaciones" en vez de confiar en el
        // contador que el listener solo incrementaba (nunca bajaba el estado anterior al cambiar).
        List<Document> pipeline = List.of(
                new Document("$match", new Document("_id", donadorId)),
                Document.parse("""
                        { $lookup: {
                            from: 'donaciones',
                            localField: '_id',
                            foreignField: 'donadorId',
                            as: 'donacionesDelDonador'
                        } }
                        """),
                Document.parse("""
                        { $addFields: {
                            cantidadesPorEstado: {
                                $arrayToObject: {
                                    $map: {
                                        input: { $setUnion: ['$donacionesDelDonador.estado', []] },
                                        as: 'estado',
                                        in: {
                                            k: '$$estado',
                                            v: {
                                                $size: {
                                                    $filter: {
                                                        input: '$donacionesDelDonador',
                                                        as: 'd',
                                                        cond: { $eq: ['$$d.estado', '$$estado'] }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } }
                        """)
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