package ar.edu.utn.dds.k3003.listeners;

import ar.edu.utn.dds.k3003.dtos.CategoriaHistoricaDTO;
import ar.edu.utn.dds.k3003.documentos.*;
import ar.edu.utn.dds.k3003.events.asignacionEvents.AsignacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.depositoEvents.*;
import ar.edu.utn.dds.k3003.events.donacionEvents.DonacionActualizadaEvent;
import ar.edu.utn.dds.k3003.events.donacionEvents.DonacionConQuejaRegistradaEvent;
import ar.edu.utn.dds.k3003.events.donacionEvents.DonacionCreadaEvent;
import ar.edu.utn.dds.k3003.events.donacionEvents.DonacionEstadoActualizadoEvent;
import ar.edu.utn.dds.k3003.events.donadorEvents.*;
import ar.edu.utn.dds.k3003.events.misionEvents.MisionAgregadaEvent;
import ar.edu.utn.dds.k3003.events.misionEvents.MisionEliminadaEvent;
import ar.edu.utn.dds.k3003.events.necesidadEvents.*;
import ar.edu.utn.dds.k3003.events.productoEvents.ProductoActualizadoEvent;
import ar.edu.utn.dds.k3003.events.productoEvents.ProductoCreadoEvent;
import ar.edu.utn.dds.k3003.events.productoEvents.ProductoEliminadoEvent;
import ar.edu.utn.dds.k3003.events.productoEvents.StockProductoModificadoEvent;
import ar.edu.utn.dds.k3003.events.stockEvents.StockActualizadoEvent;
import ar.edu.utn.dds.k3003.service.Actualizador;
import ar.edu.utn.dds.k3003.service.ResumenDonador;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.time.Instant;
import java.util.ArrayList;
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

    @RabbitListener(queues = "consultas.donador-creado")
    public void onDonadorCreado(DonadorCreadoEvent e) {
        // Convertimos el ID numérico de postgres a String para que coincida con donadorId de MongoDoc
        String donadorIdStr = String.valueOf(e.getId());

        Query query = new Query(Criteria.where("donadorId").is(donadorIdStr));

        Update update = new Update()
                .set("donadorId", donadorIdStr)
                .set("nombre", e.getNombre())
                .set("apellido", e.getApellido())
                .set("edad", e.getEdad())
                .set("email", e.getEmail())
                .set("nroDocumento", e.getNroDocumento())
                .set("domicilio", e.getDomicilio())
                .set("estado", e.getEstado())
                .set("categoriaActual", e.getCategoria());

        // Upsert: si no existe el documento del donador en Mongo, lo crea con estos datos base;
        // si ya existiera por algún motivo, actualiza sus datos personales.
        mongo.upsert(query, update, DonadorDoc.class);

        System.out.println("DonadorDoc inicializado/sincronizado en MongoDB para el ID: " + donadorIdStr);
    }

    // 1) cambiar estado de donador
    @RabbitListener(queues = "consultas.donador-estado")
    public void onDonadorEstado(DonadorEstadoActualizadoEvent e) {
        actualizador.aplicarSiEsMasNuevo(DonadorDoc.class, e.donadorId(),
                "estadoActualizadoEn", e.fechaEvento(),
                new Update().set("estado", e.estado()));
    }

    @RabbitListener(queues = "consultas.donador-set-mision")
    public void onSetMisionDonador(DonadorMisionEvent e) { // Reemplaza 'TuEventoDeMision' por la clase de evento real que uses

        // Convertimos el ID a String por si en Mongo lo guardás como texto (como venís haciendo)
        String donadorIdStr = String.valueOf(e.getDonadorId()); // O e.donadorId() si tu evento es un record

        // 1. Armamos la query para buscar al donador por su ID
        Query query = new Query(Criteria.where("donadorId").is(donadorIdStr));

        // 2. Creamos el Update especificando SOLAMENTE el campo que querés modificar
        Update update = new Update().set("misionActual", e.getMisionActualId()); // Cambiá 'getMisionId()' por el método que traiga el ID de la misión en tu evento

        // 3. Ejecutamos la actualización en MongoDB (actualiza el primer documento que coincida)
        mongo.updateFirst(query, update, DonadorDoc.class);

        System.out.println(" [x] Misión actual actualizada para el donador ID: " + donadorIdStr);
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

        Query query = new Query(Criteria.where("donadorId").is(evento.donadorID()));
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


    @RabbitListener(queues = "consultas.donador-entidad-donada")
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

            System.out.println("Nueva entidad " + e.getRazonSocialEntidad() + " agregada al donador: " + e.getDonadorId());
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


    @RabbitListener(queues = "consultas.donacion-creada")
    public void onDonacionCreada(DonacionCreadaEvent e) {
        Query query = new Query(Criteria.where("_id").is(e.donacionId()));

        // Preparamos los cambios o datos iniciales (la queja arranca explícitamente en null)
        Update update = new Update()
                .setOnInsert("_id", e.donacionId())
                .set("donadorId", e.donadorId())
                .set("productoId", e.productoId())
                .set("nombreProducto", e.nombreProducto())
                .set("categoriaProducto", e.categoriaProducto())
                .set("subcategoriaProducto", e.subcategoriaProducto())
                .set("cantidad", e.cantidad())
                .set("estado", e.estado())
                .set("fechaIngreso", e.fechaIngreso())
                .set("queja", null) // <-- Arranca en null tal como pediste
                .set("ultimaActualizacion", e.ultimaActualizacion());

        // Usamos upsert para asegurar que si el evento llega duplicado no rompa, sino que actualice/inserte de forma idempotente
        mongo.upsert(query, update, DonacionDoc.class);

        System.out.println(" [x] Donación creada e indexada en MongoDB - ID: " + e.donacionId());
    }


    @RabbitListener(queues = "consultas.donacion-queja")
    public void onDonacionConQueja(DonacionConQuejaRegistradaEvent e) {

        // 1. Buscamos la donación en la colección "donaciones" por su ID
        Query query = new Query(Criteria.where("_id").is(e.donacionId()));

        // 2. Creamos el subdocumento de la queja respetando el record que definiste
        DonacionDoc.QuejaDonacionDoc nuevaQueja = new DonacionDoc.QuejaDonacionDoc(
                e.quejaId(),
                e.fechaQueja(),
                e.descripcionQueja()
        );

        // 3. Actualizamos el estado a CONQUEJA, asignamos la queja y actualizamos el timestamp
        Update update = new Update()
                .set("estado", e.nuevoEstado())
                .set("queja", nuevaQueja)
                .set("ultimaActualizacion", e.fechaEvento());

        mongo.updateFirst(query, update, DonacionDoc.class);

        System.out.println("Queja registrada y estado actualizado a CONQUEJA en DonacionDoc ID: " + e.donacionId());
    }

    @RabbitListener(queues = "consultas.donacion-estado-actualizado")
    public void onDonacionEstadoActualizado(DonacionEstadoActualizadoEvent e) {

        // 1. Buscamos la donación en la colección "donaciones" por su ID
        Query query = new Query(Criteria.where("_id").is(e.donacionId()));

        // 2. Actualizamos el estado y el timestamp de última actualización
        Update update = new Update()
                .set("estado", e.nuevoEstado())
                .set("ultimaActualizacion", e.fechaEvento());

        mongo.updateFirst(query, update, DonacionDoc.class);

        System.out.println("Estado actualizado a '" + e.nuevoEstado() + "' en DonacionDoc ID: " + e.donacionId());
    }


    // --------------------
    //      PRODUCTO
    // --------------------

    @RabbitListener(queues = "consultas.producto-creado")
    public void onProductoCreado(ProductoCreadoEvent e) {
        Query query = new Query(Criteria.where("productoId").is(e.getProductoId()));

        Update update = new Update()
                .set("productoId", e.getProductoId())
                .set("nombre", e.getNombre())
                .set("descripcion", e.getDescripcion())
                .set("categoriaId", e.getCategoriaId())
                .set("categoriaNombre", e.getCategoriaNombre())
                .set("subcategoriaId", e.getSubcategoriaId())
                .set("subcategoriaNombre", e.getSubcategoriaNombre())
                .set("identificadorId", e.getIdentificadorId())
                .set("identificadorTipo", e.getIdentificadorTipo())
                .set("ultimaActualizacion", Instant.now());

        mongo.upsert(query, update, ProductoDoc.class);
        System.out.println(" [x] ProductoDoc enriquecido guardado en MongoDB para ID: " + e.getProductoId());
    }

    @RabbitListener(queues = "consultas.producto-actualizado")
    public void onProductoActualizado(ProductoActualizadoEvent e) {
        Query query = new Query(Criteria.where("productoId").is(e.getProductoId()));

        Update update = new Update()
                .set("nombre", e.getNombre())
                .set("descripcion", e.getDescripcion())
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, ProductoDoc.class);
        System.out.println(" [x] ProductoDoc actualizado en MongoDB para ID: " + e.getProductoId());
    }

    @RabbitListener(queues = "consultas.producto-eliminado")
    public void onProductoEliminado(ProductoEliminadoEvent e) {
        Query query = new Query(Criteria.where("productoId").is(e.getProductoId()));
        mongo.remove(query, ProductoDoc.class);
        System.out.println(" [x] ProductoDoc eliminado de MongoDB para ID: " + e.getProductoId());
    }


    @RabbitListener(queues = "consultas.producto-stock-modificado")
    public void onStockModificado(StockProductoModificadoEvent e) {

        // 1. Verificamos si el producto ya tiene registrado este depósito en su lista
        Query queryConDeposito = new Query(
                Criteria.where("productoId").is(e.getProductoId())
                        .and("depositosStock.depositoId").is(e.getDepositoId())
        );

        boolean yaExisteDeposito = mongo.exists(queryConDeposito, ProductoDoc.class);

        if (yaExisteDeposito) {
            // 2A. Si ya existe, incrementamos/decrementamos las unidades de forma atómica con '$'
            Update update = new Update().inc("depositosStock.$.cantidadUnidades", e.getCantidadDelta());
            mongo.updateFirst(queryConDeposito, update, ProductoDoc.class);

            System.out.println(" [x] Stock actualizado para producto " + e.getProductoId() + " en depósito " + e.getDepositoId());
        } else {
            // 2B. Si no existe, agregamos el nuevo objeto al array con .push()
            Query queryProducto = new Query(Criteria.where("productoId").is(e.getProductoId()));

            ProductoDoc.DepositoStockDoc nuevoStock = new ProductoDoc.DepositoStockDoc();
            nuevoStock.setDepositoId(e.getDepositoId());
            nuevoStock.setNombreDeposito(e.getNombreDeposito());
            nuevoStock.setCantidadUnidades(e.getCantidadDelta());

            Update update = new Update().push("depositosStock", nuevoStock);
            mongo.updateFirst(queryProducto, update, ProductoDoc.class);

            System.out.println(" [x] Nuevo depósito " + e.getDepositoId() + " agregado con stock al producto " + e.getProductoId());
        }
    }

    // -----------------------
    //      NECESIDAD
    // ----------------------
    @RabbitListener(queues = "consultas.necesidad-creada")
    public void onNecesidadCreada(NecesidadCreadaEvent e) {
        Query query = new Query(Criteria.where("id").is(e.getId()));

        Update update = new Update()
                .set("id", e.getId())
                .set("entidadId", e.getEntidadId())
                .set("razonSocial", e.getRazonSocial())
                .set("nivelUrgencia", e.getNivelUrgencia())
                .set("descripcion", e.getDescripcion())
                .set("tipoNecesidad", e.getTipoNecesidad())
                .set("productoId", e.getProductoId())
                .set("nombreProducto", e.getNombreProducto())
                .set("categoriaProducto", e.getCategoriaProducto())
                .set("subcategoriaProducto", e.getSubcategoriaProducto())
                .set("cantidadObjetivo", e.getCantidadObjetivo())
                .setOnInsert("cantidadRecibida", 0) // Inicializa en 0 solo si es documento nuevo
                .setOnInsert("asignacionesPendientes", new ArrayList<>()) // Inicializa lista vacía
                .set("ultimaActualizacion", Instant.now());

        // Upsert por si acaso ya existiera
        mongo.upsert(query, update, NecesidadDoc.class);
        System.out.println("NecesidadDoc creada/sincronizada en MongoDB para ID: " + e.getId());
    }

    @RabbitListener(queues = "consultas.necesidad-actualizada")
    public void onNecesidadActualizada(NecesidadActualizadaEvent e) {
        Query query = new Query(Criteria.where("id").is(e.getId()));

        Update update = new Update()
                .set("nivelUrgencia", e.getNivelUrgencia())
                .set("descripcion", e.getDescripcion())
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, NecesidadDoc.class);
        System.out.println("NecesidadDoc actualizada en MongoDB para ID: " + e.getId());
    }

    @RabbitListener(queues = "consultas.necesidad-eliminada")
    public void onNecesidadEliminada(NecesidadEliminadaEvent e) {
        Query query = new Query(Criteria.where("id").is(e.getId()));
        mongo.remove(query, NecesidadDoc.class);
        System.out.println("NecesidadDoc eliminada de MongoDB para ID: " + e.getId());
    }

    @RabbitListener(queues = "consultas.asignacion-completada")
    public void onAsignacionCompletada(NecesidadAsignacionHecha e) {

        // 1. Buscamos la necesidad contenedora
        Query query = new Query(Criteria.where("id").is(e.getNecesidadId()));

        // 2. Usamos .pull() para remover del array 'asignacionesPendientes'
        // el objeto que tenga el asignacionId correspondiente
        Update update = new Update().pull("asignacionesPendientes",
                Query.query(Criteria.where("asignacionId").is(e.getAsignacionId()))
        );

        mongo.updateFirst(query, update, NecesidadDoc.class);

        System.out.println("signación pendiente " + e.getAsignacionId() + " eliminada de la necesidad: " + e.getNecesidadId());
    }


    @RabbitListener(queues = "consultas.necesidad-satisfecha")
    public void onNecesidadSatisfecha(CantidadRecibidaActualizadaEvent e) {

        // 1. Buscamos la necesidad por su ID de String (coincidente con el record NecesidadDoc)
        Query query = new Query(Criteria.where("id").is(e.getNecesidadId()));

        // 2. Incrementamos la cantidad recibida e actualizamos la fecha de última modificación
        Update update = new Update()
                .inc("cantidadRecibida", e.getCantidad())
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, NecesidadDoc.class);

        System.out.println("Cantidad recibida incrementada en " + e.getCantidad() + " para la necesidad: " + e.getNecesidadId());
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
    @RabbitListener(queues = "consultas.deposito-creado")
    public void onDepositoCreado(DepositoCreadoEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        Update update = new Update()
                .set("depositoId", e.getDepositoId())
                .set("nombre", e.getNombre())
                .set("direccion", e.getDireccion())
                .set("algoritmoMatchmaking", e.getAlgoritmoMatchmaking())
                .set("capacidadMaxima", e.getCapacidadMaxima())
                .set("ultimaActualizacion", Instant.now());

        mongo.upsert(query, update, DepositoDoc.class);
        System.out.println("DepositoDoc creado/sincronizado en MongoDB para ID: " + e.getDepositoId());
    }

    @RabbitListener(queues = "consultas.deposito-eliminado")
    public void onDepositoEliminado(DepositoEliminadoEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));
        mongo.remove(query, DepositoDoc.class);
        System.out.println("DepositoDoc eliminado de MongoDB para ID: " + e.getDepositoId());
    }

    @RabbitListener(queues = "consultas.deposito-algoritmo-actualizado")
    public void onAlgoritmoActualizado(DepositoAlgoritmoActualizadoEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        Update update = new Update()
                .set("algoritmoMatchmaking", e.getAlgoritmoMatchmaking())
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, DepositoDoc.class);
        System.out.println("DepositoDoc actualizado (algoritmo) en MongoDB para ID: " + e.getDepositoId());
    }

    @RabbitListener(queues = "consultas.deposito-paquete-agregado")
    public void onPaqueteAgregado(DepositoPaqueteAgregadoEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        DepositoDoc.PaqueteStockDoc nuevoPaquete = new DepositoDoc.PaqueteStockDoc(
                e.getPaqueteId(),
                e.getDonacionID(),
                e.getProductoID(),
                e.getCantidad()
        );

        Update update = new Update()
                .push("stockActual", nuevoPaquete)
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, DepositoDoc.class);
        System.out.println("Paquete agregado al DepositoDoc en MongoDB para depósito ID: " + e.getDepositoId());
    }

    @RabbitListener(queues = "consultas.deposito-paquete-actualizado")
    public void onPaqueteStockActualizado(DepositoPaqueteStockActualizadoEvent e) {
        Query queryDeposito = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        // 1. Descontamos la cantidad del paquetesDeposito general del depósito
        Update updatePaquetesDeposito = new Update()
                .inc("paquetesDeposito", -e.getCantidadDescontada())
                .set("ultimaActualizacion", Instant.now());
        mongo.updateFirst(queryDeposito, updatePaquetesDeposito, DepositoDoc.class);

        // 2. Evaluamos si el paquete se quedó sin stock o todavía tiene unidades
        if (e.getNuevaCantidad() <= 0) {
            // Si se agotó, lo removemos del array stockActual
            Query queryPaquete = new Query(
                    Criteria.where("depositoId").is(e.getDepositoId())
                            .and("stockActual.paqueteId").is(e.getPaqueteId())
            );
            Update updatePull = new Update().pull("stockActual", org.bson.Document.parse("{\"paqueteId\": \"" + e.getPaqueteId() + "\"}"));
            mongo.updateFirst(queryPaquete, updatePull, DepositoDoc.class);

            System.out.println("Paquete agotado y removido del DepositoDoc ID: " + e.getDepositoId());
        } else {
            // Si todavía tiene stock, actualizamos la cantidad en el subdocumento usando el operador posicional '$'
            Query queryPaquete = new Query(
                    Criteria.where("depositoId").is(e.getDepositoId())
                            .and("stockActual.paqueteId").is(e.getPaqueteId())
            );
            Update updateSet = new Update().set("stockActual.$.cantidad", e.getNuevaCantidad());
            mongo.updateFirst(queryPaquete, updateSet, DepositoDoc.class);

            System.out.println("Cantidad de paquete actualizada en DepositoDoc ID: " + e.getDepositoId());
        }
    }

    @RabbitListener(queues = "consultas.deposito-entidad-atendida")
    public void onEntidadAtendida(DepositoEntidadAtendidaEvent e) {
        Query queryDeposito = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        // 1. Verificamos si la entidad ya existe dentro de la lista 'entidadesAtendidas' del depósito
        Query queryEntidadExistente = new Query(
                Criteria.where("depositoId").is(e.getDepositoId())
                        .and("entidadesAtendidas.entidadId").is(e.getEntidadId())
        );

        boolean yaFueAtendida = mongo.exists(queryEntidadExistente, DepositoDoc.class);

        if (yaFueAtendida) {
            // 2A. Si ya fue atendida, sumamos las unidades entregadas con el operador posicional '$'
            Update updateInc = new Update()
                    .inc("entidadesAtendidas.$.unidadesEntregadas", e.getUnidadesEntregadas())
                    .set("ultimaActualizacion", Instant.now());

            mongo.updateFirst(queryEntidadExistente, updateInc, DepositoDoc.class);
            System.out.println("Unidades sumadas a entidad existente en DepositoDoc ID: " + e.getDepositoId());
        } else {
            // 2B. Si es una entidad nueva para este depósito, la pusheamos al array Y aumentamos el contador total en 1
            DepositoDoc.EntidadesAtendidasDoc nuevaEntidad = new DepositoDoc.EntidadesAtendidasDoc(
                    e.getEntidadId(),
                    e.getRazonSocial(),
                    e.getUnidadesEntregadas()
            );

            Update updatePush = new Update()
                    .push("entidadesAtendidas", nuevaEntidad)
                    .inc("cantidadDeEntidadesDistintasAtendidas", 1)
                    .set("ultimaActualizacion", Instant.now());

            mongo.updateFirst(queryDeposito, updatePush, DepositoDoc.class);
            System.out.println("Nueva entidad agregada y contador incrementado en DepositoDoc ID: " + e.getDepositoId());
        }
    }


    @RabbitListener(queues = "consultas.deposito-cantidad-asignacion-estado")
    public void onAsignacionEstadoReportada(DepositoCantidadAsignacionEstadoEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        // Incrementa en 1 la clave correspondiente dentro del Map en MongoDB de forma atómica
        Update update = new Update()
                .inc("cantidadAsignacionesPorEstado." + e.getEstado(), 1L)
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, DepositoDoc.class);

        System.out.println("Contador de estado [" + e.getEstado() + "] incrementado para depósito ID: " + e.getDepositoId());
    }


    @RabbitListener(queues = "consultas.deposito-donacion-ingresada")
    public void onDonacionIngresada(DepositoDonacionIngresadaEvent e) {
        Query query = new Query(Criteria.where("depositoId").is(e.getDepositoId()));

        DepositoDoc.DonacionDepositoDoc nuevaDonacion = new DepositoDoc.DonacionDepositoDoc(
                e.getDonacionId(),
                e.getDonadorId(),
                e.getCantidadDonada(),
                e.getFechaQueLlego(),
                null // Aún no se entregó
        );

        Update update = new Update()
                .push("donacionesDeposito", nuevaDonacion)
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, DepositoDoc.class);
        System.out.println("Donación agregada al historial del DepositoDoc ID: " + e.getDepositoId());
    }

    @RabbitListener(queues = "consultas.deposito-donacion-entregada")
    public void onDonacionEntregada(DepositoDonacionEntregadaEvent e) {
        // Buscamos el depósito y el elemento dentro del array donacionesDeposito que coincida con la donacionId
        Query query = new Query(
                Criteria.where("depositoId").is(e.getDepositoId())
                        .and("donacionesDeposito.donacionId").is(e.getDonacionId())
        );

        // Actualizamos la fecha de entrega usando el operador posicional '$'
        Update update = new Update()
                .set("donacionesDeposito.$.fechaQueSeEntrego", e.getFechaQueSeEntrego())
                .set("ultimaActualizacion", Instant.now());

        mongo.updateFirst(query, update, DepositoDoc.class);
        System.out.println("Fecha de entrega registrada para la donación en DepositoDoc ID: " + e.getDepositoId());
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
