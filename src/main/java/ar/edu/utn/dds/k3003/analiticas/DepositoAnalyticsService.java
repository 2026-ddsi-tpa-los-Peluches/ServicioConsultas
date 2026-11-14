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

        // $match con Document (no interpolado) para no armar el filtro a partir de un String con el ID.
        Document matchStage = new Document("$match", new Document("_id", depositoId));

        // Traemos las asignaciones del depósito para recalcular los agregados por estado y por entidad
        // a partir de la fuente de verdad (colección "asignaciones"), en vez de confiar en contadores
        // que solo se incrementaban y nunca se corregían al cambiar de estado.
        Document lookupAsignaciones = Document.parse("""
                { $lookup: {
                    from: 'asignaciones',
                    localField: '_id',
                    foreignField: 'depositoId',
                    as: 'asignacionesDeposito'
                } }
                """);

        Document addFieldsAgregados = Document.parse("""
                { $addFields: {
                    cantidadAsignacionesPorEstado: {
                        $arrayToObject: {
                            $map: {
                                input: { $setUnion: ['$asignacionesDeposito.estado', []] },
                                as: 'estado',
                                in: {
                                    k: '$$estado',
                                    v: {
                                        $size: {
                                            $filter: {
                                                input: '$asignacionesDeposito',
                                                as: 'a',
                                                cond: { $eq: ['$$a.estado', '$$estado'] }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    cantidadDeEntidadesDistintasAtendidas: {
                        $size: {
                            $setUnion: [
                                {
                                    $map: {
                                        input: {
                                            $filter: {
                                                input: '$asignacionesDeposito',
                                                as: 'a',
                                                cond: { $eq: ['$$a.estado', 'COMPLETADA'] }
                                            }
                                        },
                                        as: 'a',
                                        in: '$$a.entidadId'
                                    }
                                },
                                []
                            ]
                        }
                    }
                } }
                """);

        String projectJson = "{ $project: {" +
                "  nombre: 1," +
                "  direccion: 1," +
                "  algoritmoMatchmaking: 1," +
                "  capacidadMaxima: 1," +
                "  stockTotal: { $ifNull: ['$stockTotal', 0] }," +
                "  cantidadAsignacionesPorEstado: 1," +
                "  cantidadDeEntidadesDistintasAtendidas: 1," +
                "  ocupacionPorcentaje: {" +
                "    $cond: {" +
                "      if: { $gt: ['$capacidadMaxima', 0] }," +
                "      then: { $divide: [{ $ifNull: ['$stockTotal', 0] }, '$capacidadMaxima'] }," +
                "      else: 0" +
                "    }" +
                "  }," +
                "  donacionesFiltradas: {" +
                "    $filter: {" +
                "      input: '$donacionesDeposito'," +
                "      as: 'd'," +
                "      cond: {" +
                "        $and: [" +
                "          { $gte: ['$$d.fechaQueLlego', { \"$date\": \"" + fechaInicio.toString() + "\" }] }," +
                "          { $lte: ['$$d.fechaQueLlego', { \"$date\": \"" + fechaFin.toString() + "\" }] }," +
                "          { $ne: ['$$d.fechaQueSeEntrego', null] }" +
                "        ]" +
                "      }" +
                "    }" +
                "  }" +
                "}}";

        String addFieldsJson = "{ $addFields: {" +
                "  tiempoPromedioEntregaMilis: {" +
                "    $avg: {" +
                "      $map: {" +
                "        input: '$donacionesFiltradas'," +
                "        as: 'dv'," +
                "        in: { $subtract: ['$$dv.fechaQueSeEntrego', '$$dv.fechaQueLlego'] }" +
                "      }" +
                "    }" +
                "  }" +
                "}}";

        // 1. Armamos el pipeline con los Strings limpios
        List<Document> pipeline = List.of(
                matchStage,
                lookupAsignaciones,
                addFieldsAgregados,
                Document.parse(projectJson),
                Document.parse(addFieldsJson)
        );

        // 2. Ejecutamos directo sobre la colección usando el driver nativo de Mongo
        Document resultadoCrudo = mongoTemplate.getCollection("depositos")
                .aggregate(pipeline)
                .first();

        if (resultadoCrudo == null) {
            return null;
        }

        // 3. Convertimos el Document crudo de MongoDB al DTO de Java
        return mongoTemplate.getConverter().read(DepositoReporteDTO.class, resultadoCrudo);
    }
}