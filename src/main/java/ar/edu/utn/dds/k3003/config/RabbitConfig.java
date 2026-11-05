package ar.edu.utn.dds.k3003.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "donatrack.eventos";

    @Bean
    Declarables colasYBindings() {
        TopicExchange exchange = new TopicExchange(EXCHANGE);
        List<Declarable> elementos = new ArrayList<>();
        elementos.add(exchange);
        for (String nombre : List.of(

                //DONADOR
                "donador-creado" ,"donador-estado", "donador-incentivos", "donador-categoria",
                "mision-donador-completada","mision-donador-limpliada", "mision-donador-progreso",
                "insignia-donador-agregada", "insignia-donador-quitada",
                "donador-donacion-estado", "donador-queja-registrada", "donador-entidad-donada",

                //PRODUCTO
                "producto-creado", "producto-actualizado", "producto-eliminado", "producto-stock-modificado",


                //INSIGNNIA
                "insignia-agregada", "insignia-eliminada",

                // DONACION
                "donacion",

                // PRODUCTO
                "producto-creado", "producto-actualizado", "producto-eliminado",

                // NECESIDAD
                "necesidad-cantidad-recibida", "necesidad-asignacion-pendiente", "necesidad-asignacion-hecha",
                "necesidad-creada", "necesidad-actualizada", "necesidad-eliminada",

                // STOCK
                "stock",

                // DEPOSITO
                "deposito-creado","deposito-eliminado","deposito-algoritmo-actualizado",
                "deposito-paquete-actualizado", "deposito-paquete-agregado",

                // ASIGNACION
                "asignacion",

                //MISION
                "mision-agregada", "mision-eliminada"
        )) {
            Queue cola = QueueBuilder.durable("consultas." + nombre).build();
            elementos.add(cola);
            elementos.add(BindingBuilder.bind(cola).to(exchange).with(nombre));
        }
        return new Declarables(elementos);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}