package ar.edu.utn.dds.k3003.listeners;

import ar.edu.utn.dds.k3003.events.*;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.service.Actualizador;
import ar.edu.utn.dds.k3003.service.ResumenDonador;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class ConsultasListener {

    private final Actualizador actualizador;
    private final ResumenDonador resumen;
    private final MongoTemplate mongo;

    public ConsultasListener(Actualizador actualizador, ResumenDonador resumen, MongoTemplate mongo) {
        this.actualizador = actualizador;
        this.resumen = resumen;
        this.mongo = mongo;
    }

    @RabbitListener(queues = "consultas.donador-estado")
    public void onDonadorEstado(DonadorEstadoActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(Donador.class, e.donadorId(),
                "estadoActualizadoEn", e.fechaEvento(),
                new Update().set("estado", e.estado()));
    }

    @RabbitListener(queues = "consultas.donador-incentivos")
    public void onDonadorIncentivos(DonadorIncentivosActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(Donador.class, e.donadorId(),
                "incentivosActualizadoEn", e.fechaEvento(),
                new Update()
                        .set("categoriaActual", e.categoriaActual())
                        .set("historialCategorias", e.historialCategorias())
                        .set("insignias", e.insignias())
                        .set("misionActual", e.misionActual()));
    }

    @RabbitListener(queues = "consultas.donacion")
    public void onDonacion(DonacionActualizadaEvent e) {
        List<Donacion.Queja> quejas = e.quejas() == null ? List.of() : e.quejas();
        boolean aplicado = actualizador.aplicarSiEsMasNuevo(Donacion.class, e.donacionId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("donadorId", e.donadorId())
                        .set("productoId", e.productoId())
                        .set("nombreProducto", e.nombreProducto())
                        .set("categoriaProducto", e.categoriaProducto())
                        .set("subcategoriaProducto", e.subcategoriaProducto())
                        .set("cantidad", e.cantidad())
                        .set("estado", e.estado())
                        .set("fechaIngreso", e.fechaIngreso())
                        .set("quejas", quejas));
        if (aplicado) {
            resumen.recalcular(e.donadorId());
        }
    }

    @RabbitListener(queues = "consultas.necesidad")
    public void onNecesidad(NecesidadActualizadaEvent e) {
        actualizador.aplicarSiEsMasNuevo(Necesidad.class, e.necesidadId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("entidadId", e.entidadId())
                        .set("razonSocial", e.razonSocial())
                        .set("nivelUrgencia", e.nivelUrgencia())
                        .set("tipoNecesidad", e.tipoNecesidad())
                        .set("productoId", e.productoId())
                        .set("nombreProducto", e.nombreProducto())
                        .set("categoriaProducto", e.categoriaProducto())
                        .set("subcategoriaProducto", e.subcategoriaProducto())
                        .set("cantidadFaltante", e.cantidadFaltante()));
    }

    @RabbitListener(queues = "consultas.stock")
    public void onStock(StockActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(Stock.class,
                e.productoId() + "-" + e.depositoId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("productoId", e.productoId())
                        .set("depositoId", e.depositoId())
                        .set("nombreDeposito", e.nombreDeposito())
                        .set("cantidad", e.cantidad()));
    }

    @RabbitListener(queues = "consultas.deposito")
    public void onDeposito(DepositoActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(Deposito.class, e.depositoId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("nombre", e.nombre())
                        .set("capacidad", e.capacidad())
                        .set("algoritmoMatchmaking", e.algoritmoMatchmaking()));
    }

    @RabbitListener(queues = "consultas.asignacion")
    public void onAsignacion(AsignacionActualizadaEvent e) {
        boolean aplicado = actualizador.aplicarSiEsMasNuevo(Asignacion.class, e.asignacionId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("paqueteId", e.paqueteId())
                        .set("necesidadId", e.necesidadId())
                        .set("depositoId", e.depositoId())
                        .set("donacionId", e.donacionId())
                        .set("entidadId", e.entidadId())
                        .set("unidades", e.unidades())
                        .set("estado", e.estado())
                        .set("fechaAsignacion", e.fechaAsignacion())
                        .set("fechaIngresoDonacion", e.fechaIngresoDonacion())
                        .set("fechaEntrega", e.fechaEntrega()));
        if (aplicado) {
            Donacion donacion = mongo.findById(e.donacionId(), Donacion.class);
            if (donacion != null) {
                resumen.recalcular(donacion.donadorId());
            }
        }
    }
}