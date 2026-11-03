package ar.edu.utn.dds.k3003.listeners;

import ar.edu.utn.dds.k3003.dtos.CategoriaHistoricaDTO;
import ar.edu.utn.dds.k3003.documentos.*;
import ar.edu.utn.dds.k3003.events.asignacionEvent.AsignacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.depositoEvents.DepositoActualizadoEvent;
import ar.edu.utn.dds.k3003.events.donacionEvent.DonacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.donadorEvents.*;
import ar.edu.utn.dds.k3003.events.necesidadEvents.NecesidadActualizadaEvent;
import ar.edu.utn.dds.k3003.events.stockEvents.StockActualizadoEvent;
import ar.edu.utn.dds.k3003.service.Actualizador;
import ar.edu.utn.dds.k3003.service.ResumenDonador;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

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
    // ---------------------------
    //         DONADOR
    // --------------------------

    // 1) cambiar estado de donador
    @RabbitListener(queues = "consultas.donador-estado")
    public void onDonadorEstado(DonadorEstadoActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(DonadorDoc.class, e.donadorId(),
                "estadoActualizadoEn", e.fechaEvento(),
                new Update().set("estado", e.estado()));
    }

//    @RabbitListener(queues = "consultas.donador-incentivos")
//    public void onDonadorIncentivos(DonadorActualizadoEvent e) {
//        actualizador.aplicarSiEsMasNuevo(DonadorDoc.class, e.donadorId(),
//                "incentivosActualizadoEn", e.fechaEvento(),
//                new Update()
//                        .set("categoriaActual", e.categoriaActual())
//                        .set("historialCategorias", e.historialCategorias())
//                        .set("insignias", e.insignias())
//                        .set("misionActual", e.misionActual()));
//    }



    // 2) cambiar categoria de donador
    @RabbitListener(queues = "consultas.donador-categoria")
    public void onCategoriaActualizada(DonadorCategoriaActualizadaEvent e) {
        // 1. Creamos el registro histórico con la categoría y su fecha
        CategoriaHistoricaDTO registroHistorico = new CategoriaHistoricaDTO(
                e.nuevaCategoria(),
                e.fechaCambio() // Asegúrate de que coincida si es Instant o LocalDateTime
        );

        // 2. Preparamos los cambios que aplican si el evento es el más nuevo (actualiza el estado actual)
        Update cambiosPrincipal = new Update()
                .set("categoriaActual", e.nuevaCategoria())
                .push("historialCategorias", registroHistorico);

        // 3. Usamos tu método inteligente aplicarSiEsMasNuevo para la categoría actual y el campo de fecha.
        // (Nota: asume que en tu DonadorDoc el ID del documento es "donadorId" o "_id" según cómo lo manejes).
        boolean aplicoEstadoActual = actualizador.aplicarSiEsMasNuevo(
                DonadorDoc.class,
                e.donadorId(),
                "categoriaActualizadaEn",
                e.fechaCambio(),
                cambiosPrincipal
        );

        // 4. ¿Qué pasa si el evento que llegó era MÁS VIEJO que el actual?
        // El actualizador devuelve false porque no pisó el estado actual, PERO de todas formas
        // queremos asegurarnos de que el evento viejo no se pierda del historial cronológico.
        if (!aplicoEstadoActual) {
            Query querySoloHistorial = new Query();
            Update updateHistorial = new Update().push("historialCategorias", registroHistorico);

            // Insertamos el registro en el array del historial sin alterar la categoría actual
            mongo.updateFirst(querySoloHistorial, updateHistorial, DonadorDoc.class);
        }
    }

    // 3) cambiar progreso del donador
    @RabbitListener(queues = "consultas.mision-progreso")
    public void onProgresoActualizado(MisionDonadorProgresoActualizadoEvent e) {
        Query query = new Query(Criteria.where("donadorId").is(e.donadorId())
                .and("misionActual.misionId").is(e.misionId()));

        // Actualizamos ÚNICAMENTE el campo 'progreso' dentro del objeto embebido 'misionActual'
        Update update = new Update().set("misionActual.progreso", e.progreso());

        mongo.updateFirst(query, update, DonadorDoc.class);
    }

    // 4) cambiar mision del donador
    @RabbitListener(queues = "consultas.mision-completada")
    public void onMisionCompletada(MisionDonadorCompletadaEvent e) {
        // 1. Buscamos al donador por su ID
        Query query = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

        // 2. Preparamos la actualización:
        // - Seteamos la misión actual en null (porque ya la terminó)
        // - Hacemos un push de la misión al historial con su nombre y fecha
        Update update = new Update()
                .set("misionActual", null)
                .push("misionesCompletadasHistorial", e.getMisionCompletada());

        // 3. Ejecutamos la actualización en MongoDB
        mongo.updateFirst(query, update, DonadorDoc.class);
    }

    @RabbitListener(queues = "consultas.mision-limpiada")
    public void onMisionLimpiada(MisionDonadorLimpiadaEvent e) {
        // 1. Buscamos al donador por su ID
        Query query = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

        // 2. Preparamos la actualización:
        // - Seteamos la misión actual en null (porque ya la cancelamos)
        Update update = new Update().set("misionActual", null);

        // 3. Ejecutamos la actualización en MongoDB
        mongo.updateFirst(query, update, DonadorDoc.class);
    }


    @RabbitListener(queues = "consultas.insignia-agregada")
    public void onInsigniaAgregada(InsigniaDonadorAgregadaEvent e) {
        Query query = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

        // .push() añade el elemento al array 'insignias'
        Update update = new Update().push("insignias", e.getInsigniaId());

        mongo.updateFirst(query, update, DonadorDoc.class);
    }

    // 2. Remueve la insignia del array usando $pull de MongoDB
    @RabbitListener(queues = "consultas.insignia-quitada")
    public void onInsigniaQuitada(InsigniaDonadorQuitadaEvent e) {
        Query query = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

        // .pull() busca la coincidencia exacta en el array 'insignias' y la elimina
        Update update = new Update().pull("insignias", e.getInsigniaId());

        mongo.updateFirst(query, update, DonadorDoc.class);
    }





    //------------------------------
    //      DONACION
    //------------------------------
    @RabbitListener(queues = "consultas.donacion")
    public void onDonacion(DonacionActualizadaEvent e) {
        List<DonacionDoc.Queja> quejas = e.quejas() == null ? List.of() : e.quejas();
        boolean aplicado = actualizador.aplicarSiEsMasNuevo(DonacionDoc.class, e.donacionId(),
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

    // -----------------------
    //      NECESIDAD
    // ----------------------
    @RabbitListener(queues = "consultas.necesidad")
    public void onNecesidad(NecesidadActualizadaEvent e) {
        actualizador.aplicarSiEsMasNuevo(NecesidadDoc.class, e.necesidadId(),
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

    // --------------
    //      STOCK
    // -------------
    @RabbitListener(queues = "consultas.stock")
    public void onStock(StockActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(StockDoc.class,
                e.productoId() + "-" + e.depositoId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("productoId", e.productoId())
                        .set("depositoId", e.depositoId())
                        .set("nombreDeposito", e.nombreDeposito())
                        .set("cantidad", e.cantidad()));
    }

    // ---------------
    //    DEPOSITO
    // ---------------
    @RabbitListener(queues = "consultas.deposito")
    public void onDeposito(DepositoActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(DepositoDoc.class, e.depositoId(),
                "ultimaActualizacion", e.fechaEvento(),
                new Update()
                        .set("nombre", e.nombre())
                        .set("capacidad", e.capacidad())
                        .set("algoritmoMatchmaking", e.algoritmoMatchmaking()));
    }


    // ------------
    //  ASIGNACION
    // ------------
    @RabbitListener(queues = "consultas.asignacion")
    public void onAsignacion(AsignacionActualizadaEvent e) {
        boolean aplicado = actualizador.aplicarSiEsMasNuevo(AsignacionDoc.class, e.asignacionId(),
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
            DonacionDoc donacion = mongo.findById(e.donacionId(), DonacionDoc.class);
            if (donacion != null) {
                resumen.recalcular(donacion.donadorId());
            }
        }
    }




}
