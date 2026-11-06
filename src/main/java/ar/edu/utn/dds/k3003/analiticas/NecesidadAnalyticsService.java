package ar.edu.utn.dds.k3003.analiticas;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NecesidadAnalyticsService {

    private final MongoTemplate mongoTemplate;

    public NecesidadAnalyticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Document> obtenerReporteNecesidadesInsatisfechas(
            Integer urgenciaMinima,
            String tipoNecesidad,
            String categoriaProducto,
            String subcategoriaProducto
    ) {
        // Construimos dinámicamente las condiciones del $match
        List<String> matchConditions = new ArrayList<>();
        
        // Filtro base: Necesidades insatisfechas (cantidadRecibida < cantidadObjetivo)
        matchConditions.add(" $expr: { $lt: ['$cantidadRecibida', '$cantidadObjetivo'] } ");

        if (urgenciaMinima != null) {
            matchConditions.add(" nivelUrgencia: { $gte: " + urgenciaMinima + " } ");
        }
        if (tipoNecesidad != null && !tipoNecesidad.isBlank()) {
            matchConditions.add(" tipoNecesidad: '" + tipoNecesidad + "' ");
        }
        if (categoriaProducto != null && !categoriaProducto.isBlank()) {
            matchConditions.add(" categoriaProducto: '" + categoriaProducto + "' ");
        }
        if (subcategoriaProducto != null && !subcategoriaProducto.isBlank()) {
            matchConditions.add(" subcategoriaProducto: '" + subcategoriaProducto + "' ");
        }

        String matchQuery = "{ $match: { " + String.join(", ", matchConditions) + " } }";

        // Pipeline de agregación de MongoDB
        List<Document> pipeline = List.of(
                // 1. Match con filtros y exclusión de satisfechas
                Document.parse(matchQuery),

                // 2. Ordenamiento por urgencia descendente (mayor urgencia primero)
                Document.parse("{ $sort: { nivelUrgencia: -1 } }"),

                // 3. Lookup (Join) con la colección de productos para traer el stock por depósito
                Document.parse("""
                {
                    $lookup: {
                        from: 'productos',
                        localField: 'productoId',
                        foreignField: 'productoId',
                        as: 'infoProducto'
                    }
                }
                """),

                // 4. Unwind para aplanar el resultado del lookup (si existe el producto)
                Document.parse("{ $unwind: { path: '$infoProducto', preserveNullAndEmptyArrays: true } }"),

                // 5. Project final para armar la estructura limpia solicitada
                Document.parse("""
                {
                    $project: {
                        necesidadId: '$_id',
                        entidadId: 1,
                        razonSocial: 1,
                        descripcion: 1,
                        nivelUrgencia: 1,
                        tipoNecesidad: 1,
                        productoId: 1,
                        nombreProducto: 1,
                        categoriaProducto: 1,
                        subcategoriaProducto: 1,
                        cantidadObjetivo: 1,
                        cantidadRecibida: 1,
                        cantidadFaltante: { $subtract: ['$cantidadObjetivo', '$cantidadRecibida'] },
                        stockPorDeposito: { $ifNull: ['$infoProducto.depositosStock', []] },
                        asignacionesPendientes: { $ifNull: ['$asignacionesPendientes', []] }
                    }
                }
                """)
        );

        // Ejecutamos la agregación cruda y devolvemos la lista de documentos
        return mongoTemplate.getCollection("necesidades")
                .aggregate(pipeline)
                .into(new ArrayList<>());
    }
}