package ar.edu.utn.dds.k3003.analiticas;



import ar.edu.utn.dds.k3003.dtos.DepositoReporteDTO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DepositoAnalyticsService {

    private final MongoTemplate mongoTemplate;

    public DepositoAnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public DepositoReporteDTO obtenerReporteDeposito(String depositoId, Instant fechaInicio, Instant fechaFin) {

        // 1. Armamos el pipeline con los Document crutos de MongoDB
        List<Document> pipeline = List.of(
                Document.parse("{ $match: { depositoId: '" + depositoId + "' } }"),
                Document.parse("""
                {
                    $project: {
                        nombre: 1,
                        direccion: 1,
                        algoritmoMatchmaking: 1,
                        capacidadMaxima: 1,
                        stockTotal: 1,
                        cantidadAsignacionesPorEstado: 1,
                        cantidadDeEntidadesDistintasAtendidas: 1,
                        entidadesAtendidas: 1,
                        ocupacionPorcentaje: {
                            $cond: {
                                if: { $gt: ['$capacidadMaxima', 0] },
                                then: { $divide: [{ $ifNull: ['$stockTotal', 0] }, '$capacidadMaxima'] },
                                else: 0
                            }
                        },
                        donacionesFiltradas: {
                            $filter: {
                                input: '$donacionesDeposito',
                                as: 'd',
                                cond: {
                                    $and: [
                                        { $gte: ['$$d.fechaQueLlego', ISODate('" + fechaInicio.toString() + "')] },
                                        { $lte: ['$$d.fechaQueLlego', ISODate('" + fechaFin.toString() + "')] },
                                        { $ne: ['$$d.fechaQueSeEntrego', null] }
                                    ]
                                }
                            }
                        }
                    }
                }
                """),
                Document.parse("""
                {
                    $addFields: {
                        tiempoPromedioEntregaMilis: {
                            $avg: {
                                $map: {
                                    input: '$donacionesFiltradas',
                                    as: 'dv',
                                    in: { $subtract: ['$$dv.fechaQueSeEntrego', '$$dv.fechaQueLlego'] }
                                }
                            }
                        }
                    }
                }
                """)
        );

        // 2. Ejecutamos directo sobre la colección usando el driver nativo de Mongo y mapeamos con el conversor
        Document resultadoCrudo = mongoTemplate.getCollection("depositos")
                .aggregate(pipeline)
                .first(); // Como filtramos por un ID único, tomamos el primer resultado

        if (resultadoCrudo == null) {
            return null;
        }

        // 3. Convertimos el Document crudo de MongoDB al DTO de Java usando el convertidor de Spring
        return mongoTemplate.getConverter().read(DepositoReporteDTO.class, resultadoCrudo);
    }
}
