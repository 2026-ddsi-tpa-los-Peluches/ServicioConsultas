package ar.edu.utn.dds.k3003.listeners;

import ar.edu.utn.dds.k3003.dtos.CategoriaHistoricaDTO;
import ar.edu.utn.dds.k3003.documentos.*;
import ar.edu.utn.dds.k3003.events.asignacionEvents.AsignacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.depositoEvents.DepositoActualizadoEvent;
import ar.edu.utn.dds.k3003.events.donacionEvents.DonacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.donadorEvents.*;
import ar.edu.utn.dds.k3003.events.misionEvents.MisionAgregadaEvent;
import ar.edu.utn.dds.k3003.events.misionEvents.MisionEliminadaEvent;
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
    @RabbitListener(queues = "consultas.mision-donador-progreso")
    public void onProgresoActualizado(MisionDonadorProgresoActualizadoEvent e) {
        Query query = new Query(Criteria.where("donadorId").is(e.donadorId())
                .and("misionActual.misionId").is(e.misionId()));

        // Actualizamos ÚNICAMENTE el campo 'progreso' dentro del objeto embebido 'misionActual'
        Update update = new Update().set("misionActual.progreso", e.progreso());

        mongo.updateFirst(query, update, DonadorDoc.class);
    }

    // 4) cambiar mision del donador
    @RabbitListener(queues = "consultas.mision-donador-completada")
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

    @RabbitListener(queues = "consultas.mision-donador-limpliada")
    public void onMisionLimpiada(MisionDonadorLimpiadaEvent e) {
        // 1. Buscamos al donador por su ID
        Query query = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

        // 2. Preparamos la actualización:
        // - Seteamos la misión actual en null (porque ya la cancelamos)
        Update update = new Update().set("misionActual", null);

        // 3. Ejecutamos la actualización en MongoDB
        mongo.updateFirst(query, update, DonadorDoc.class);
    }

    @RabbitListener(queues = "consultas.donador-donacion-estado")
    public void onDonadorDonacionEstado(ActualizarCantidadDonacionesEstadoEvent evento) {

        // Apunta al campo del mapa dentro del documento, ej: "cantidadesPorEstado.INGRESADA"
        String campoEstado = "cantidadesPorEstado." + evento.estado();

        Query query = new Query(Criteria.where("donadorID").is(evento.donadorID()));
        Update update = new Update().inc(campoEstado, 1);

        // upsert asegura que si el documento del donador no tiene inicializado el mapa o no existe en la vista, lo genere de forma atómica
        mongo.upsert(query, update, DonadorDoc.class);
    }


    @RabbitListener(queues = "consultas.donador-queja-registrada")
    public void onQuejaRegistrada(QuejaDonadorRegistradaEvent evento) {

        // Mapeamos el evento al subdocumento de Mongo
        DonadorDoc.QuejaDoc quejaDoc = new DonadorDoc.QuejaDoc();
        quejaDoc.setQuejaId(evento.quejaId());
        quejaDoc.setDonacionId(evento.donacionId());
        quejaDoc.setMotivo(evento.motivo());
        quejaDoc.setFecha(evento.fecha());

        // Buscamos por donadorID (convertido a Long o String según cómo lo guardes)
        Long donadorIdLong = Long.valueOf(evento.donadorId());
        Query query = new Query(Criteria.where("donadorID").is(donadorIdLong));

        // $push añade el elemento al array "quejas" de forma atómica
        Update update = new Update().push("quejas", quejaDoc);

        mongo.upsert(query, update, DonadorDoc.class);
    }


    @RabbitListener(queues = "consultas.donacion-donador-entregada")
    public void onDonacionEntregada(DonacionDonadorEntregadaEvent e) {

        // 1. Verificamos si el donador ya tiene a esta entidad en su lista
        Query queryConEntidad = new Query(
                Criteria.where("donadorId").is(e.getDonadorId())
                        .and("entidadesBeneficiadas.entidadId").is(e.getEntidadId())
        );

        boolean yaExisteEntidad = mongo.exists(queryConEntidad, DonadorDoc.class);

        if (yaExisteEntidad) {
            // Si ya le había donado, incrementamos de forma atómica las unidades usando '$'
            Update update = new Update().inc("entidadesBeneficiadas.$.unidadesEntregadas", e.getCantidad());
            mongo.updateFirst(queryConEntidad, update, DonadorDoc.class);

            System.out.println(" [x] Unidades incrementadas para la entidad " + e.getEntidadId() + " del donador: " + e.getDonadorId());
        } else {
            // Si no existe, creamos el objeto con la estructura y lo pusheamos al array
            Query queryDonador = new Query(Criteria.where("donadorId").is(e.getDonadorId()));

            DonadorDoc.EntidadBeneficiadaDoc nuevaEntidad = new DonadorDoc.EntidadBeneficiadaDoc();
            nuevaEntidad.setEntidadId(e.getEntidadId());
            nuevaEntidad.setRazonSocialEntidad(e.getRazonSocialEntidad());
            nuevaEntidad.setUnidadesEntregadas(e.getCantidad());

            Update update = new Update().push("entidadesBeneficiadas", nuevaEntidad);
            mongo.updateFirst(queryDonador, update, DonadorDoc.class);

            System.out.println(" [x] Nueva entidad " + e.getRazonSocialEntidad() + " agregada al donador: " + e.getDonadorId());
        }
    }



    // ------------------
    //      INSIGNIAS
    // ------------------


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



    //-------------------------------
    //      MISIONES
    //-------------------------------
    @RabbitListener(queues = "consultas.mision-agregada")
    public void onMisionSincronizada(MisionAgregadaEvent e) {
        Query query = new Query(Criteria.where("misionId").is(e.getMisionId()));

        Update update = new Update()
                .set("nombre", e.getNombre())
                .set("insigniaId", e.getInsigniaId())
                .set("categoriaInicio", e.getCategoriaInicio())
                .set("categoriaFin", e.getCategoriaFin())
                .set("tipoMision", e.getTipoMision());

        // Upsert actualiza si existe, o inserta si es nueva
        mongo.upsert(query, update, MisionDoc.class);
    }

    @RabbitListener(queues = "consultas.mision-eliminada")
    public void onMisionEliminada(MisionEliminadaEvent e) {
        Query query = new Query(Criteria.where("misionId").is(e.getMisionId()));

        // Elimina el documento del catálogo de MongoDB
        mongo.remove(query, MisionDoc.class);
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
