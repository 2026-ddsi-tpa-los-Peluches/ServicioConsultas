package ar.edu.utn.dds.k3003.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {

    private final MongoTemplate mongo;

    public ConsultaService(MongoTemplate mongo) { this.mongo = mongo; }

    public List<Document> calidadPorCategoria() {
        List<Document> pipeline = List.of(
                Document.parse("{ $match: { estado: { $in: ['ACEPTADA','CONQUEJA'] } } }"),
                Document.parse("""
                { $group: {
                    _id: { categoria: '$categoriaProducto', subcategoria: '$subcategoriaProducto' },
                    total: { $sum: 1 },
                    conQueja: { $sum: { $cond: [ { $eq: ['$estado','CONQUEJA'] }, 1, 0 ] } } } }"""),
                Document.parse("{ $project: { total: 1, conQueja: 1, proporcion: { $divide: ['$conQueja','$total'] } } }"),
                Document.parse("{ $sort: { proporcion: -1 } }")
        );
        return mongo.getCollection("donaciones").aggregate(pipeline).into(new ArrayList<>());
    }
}