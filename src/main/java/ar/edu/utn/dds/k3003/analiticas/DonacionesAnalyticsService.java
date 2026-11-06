package ar.edu.utn.dds.k3003.analiticas;

import ar.edu.utn.dds.k3003.dtos.ReporteCalidadYDonadoresDTO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonacionesAnalyticsService {

    private final MongoTemplate mongoTemplate;

    public DonacionesAnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public ReporteCalidadYDonadoresDTO obtenerReporteCompleto(int limiteDonadores) {
        // 1. Calculamos la proporción de quejas por categoría y subcategoría
        List<Document> pipelineQuejas = List.of(
                Document.parse("""
                {
                    $group: {
                        _id: {
                            categoria: '$categoriaProducto',
                            subcategoria: '$subcategoriaProducto'
                        },
                        totalDonaciones: { $sum: 1 },
                        totalConQueja: {
                            $sum: {
                                $cond: [ { $eq: ['$estado', 'CONQUEJA'] }, 1, 0 ]
                            }
                        }
                    }
                }
                """),
                Document.parse("""
                {
                    $project: {
                        _id: 0,
                        categoria: '$_id.categoria',
                        subcategoria: '$_id.subcategoria',
                        totalDonaciones: 1,
                        totalConQueja: 1,
                        proporcionQuejas: {
                            $cond: {
                                if: { $gt: ['$totalDonaciones', 0] },
                                then: { $divide: ['$totalConQueja', '$totalDonaciones'] },
                                else: 0
                            }
                        }
                    }
                }
                """),
                Document.parse("{ $sort: { proporcionQuejas: -1 } }")
        );

        List<Document> proporcionQuejas = mongoTemplate.getCollection("donaciones")
                .aggregate(pipelineQuejas)
                .into(new ArrayList<>());

        // 2. Obtenemos el ranking de donadores con más quejas
        List<Document> pipelineDonadores = List.of(
                Document.parse("""
                {
                    $project: {
                        donadorId: '$_id',
                        estado: 1,
                        categoriaActual: 1,
                        cantidadQuejas: { $size: { $ifNull: ['$quejas', []] } }
                    }
                }
                """),
                Document.parse("{ $sort: { cantidadQuejas: -1 } }"),
                Document.parse("{ $limit: " + limiteDonadores + " }")
        );

        List<Document> donadoresConQuejas = mongoTemplate.getCollection("donadores")
                .aggregate(pipelineDonadores)
                .into(new ArrayList<>());

        // 3. Devolvemos todo unificado en un solo objeto
        return new ReporteCalidadYDonadoresDTO(proporcionQuejas, donadoresConQuejas);
    }
}