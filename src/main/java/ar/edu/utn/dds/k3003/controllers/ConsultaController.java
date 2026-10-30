package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.service.ConsultaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bson.Document;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) { this.service = service; }

    @GetMapping("/calidad")
    public List<Document> calidad() { return service.calidadPorCategoria(); }
}