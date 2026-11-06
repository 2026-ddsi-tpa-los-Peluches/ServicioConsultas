package ar.edu.utn.dds.k3003.dtos;

import org.bson.Document;
import java.util.List;

public record ReporteCalidadYDonadoresDTO(
        List<Document> proporcionQuejasPorCategoria,
        List<Document> donadoresConMasQuejas
) {}