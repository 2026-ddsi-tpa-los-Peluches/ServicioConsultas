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
        List<Document> pipeline = new ArrayList<>();

        // Filtro base (Documents, no Strings interpolados: evita romper la consulta o inyectar
        // operadores si el filtro trae comillas): necesidades insatisfechas + filtros opcionales
        // que no dependen de la categoría/subcategoría resuelta (esos van después del $lookup).
        Document matchBase = new Document(
                "$expr", new Document("$lt", List.of("$cantidadRecibida", "$cantidadObjetivo")));
        if (urgenciaMinima != null) {
            matchBase.append("nivelUrgencia", new Document("$gte", urgenciaMinima));
        }
        if (tipoNecesidad != null && !tipoNecesidad.isBlank()) {
            matchBase.append("tipoNecesidad", tipoNecesidad);
        }
        pipeline.add(new Document("$match", matchBase));

        // Ordenamiento por urgencia descendente (mayor urgencia primero)
        pipeline.add(Document.parse("{ $sort: { nivelUrgencia: -1 } }"));

        // Lookup (Join) con la colección de productos: de ahí sacamos el stock por depósito y,
        // como el producto sí tiene la categoría/subcategoría resueltas (igual que en Consulta 4),
        // también resolvemos categoriaProducto/subcategoriaProducto en vez de confiar en el campo
        // crudo de la necesidad (que llega sin la subcategoría).
        pipeline.add(Document.parse("""
                {
                    $lookup: {
                        from: 'productos',
                        localField: 'productoId',
                        foreignField: 'productoId',
                        as: 'infoProducto'
                    }
                }
                """));
        pipeline.add(Document.parse("{ $unwind: { path: '$infoProducto', preserveNullAndEmptyArrays: true } }"));

        // Lookup con las asignaciones de Logística: las pendientes (ASIGNADA, todavía no entregadas)
        // de esta necesidad se calculan desde la colección "asignaciones" (la misma que usa la
        // Consulta 3), en vez de depender del array asignacionesPendientes que se mantenía a mano
        // con push/pull en el listener.
        pipeline.add(Document.parse("""
                {
                    $lookup: {
                        from: 'asignaciones',
                        localField: '_id',
                        foreignField: 'necesidadId',
                        as: 'asignacionesDeLaNecesidad'
                    }
                }
                """));

        pipeline.add(Document.parse("""
                {
                    $addFields: {
                        categoriaProductoResuelta: { $ifNull: ['$infoProducto.categoriaNombre', '$categoriaProducto'] },
                        subcategoriaProductoResuelta: { $ifNull: ['$infoProducto.subcategoriaNombre', '$subcategoriaProducto'] },
                        asignacionesPendientesResueltas: {
                            $map: {
                                input: {
                                    $filter: {
                                        input: '$asignacionesDeLaNecesidad',
                                        as: 'a',
                                        cond: { $eq: ['$$a.estado', 'ASIGNADA'] }
                                    }
                                },
                                as: 'a',
                                in: { asignacionId: '$$a._id', depositoId: '$$a.depositoId', unidades: '$$a.unidades' }
                            }
                        }
                    }
                }
                """));

        // Filtros de categoría/subcategoría: se aplican sobre el valor ya resuelto para aceptar
        // tanto el id (como lo tiene la necesidad o el producto) como el nombre.
        List<Document> postMatchConditions = new ArrayList<>();
        if (categoriaProducto != null && !categoriaProducto.isBlank()) {
            postMatchConditions.add(new Document("$or", List.of(
                    new Document("categoriaProducto", categoriaProducto),
                    new Document("categoriaProductoResuelta", categoriaProducto),
                    new Document("infoProducto.categoriaId", categoriaProducto))));
        }
        if (subcategoriaProducto != null && !subcategoriaProducto.isBlank()) {
            postMatchConditions.add(new Document("$or", List.of(
                    new Document("subcategoriaProductoResuelta", subcategoriaProducto),
                    new Document("infoProducto.subcategoriaId", subcategoriaProducto))));
        }
        if (!postMatchConditions.isEmpty()) {
            Document postMatch = postMatchConditions.size() == 1
                    ? postMatchConditions.get(0)
                    : new Document("$and", postMatchConditions);
            pipeline.add(new Document("$match", postMatch));
        }

        // Project final para armar la estructura limpia solicitada
        pipeline.add(Document.parse("""
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
                        categoriaProducto: '$categoriaProductoResuelta',
                        subcategoriaProducto: '$subcategoriaProductoResuelta',
                        cantidadObjetivo: 1,
                        cantidadRecibida: 1,
                        cantidadFaltante: { $subtract: ['$cantidadObjetivo', '$cantidadRecibida'] },
                        stockPorDeposito: { $ifNull: ['$infoProducto.depositosStock', []] },
                        asignacionesPendientes: '$asignacionesPendientesResueltas'
                    }
                }
                """));

        // Ejecutamos la agregación cruda y devolvemos la lista de documentos
        return mongoTemplate.getCollection("necesidades")
                .aggregate(pipeline)
                .into(new ArrayList<>());
    }
}