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
                "donador-creado" ,"donador-estado", "donador-incentivos", "donador-categoria",
                "mision-donador-completada","mision-donador-limpliada", "mision-donador-progreso",
                "insignia-donador-agregada", "insignia-donador-quitada",
                "producto-creado", "producto-actualizado", "producto-eliminado",
                "donador-donacion-estado", "donador-queja-registrada", "donador-entidad-donada",
                "insignia-agregada", "insignia-eliminada",
                "donacion",
                "producto-creado", "producto-actualizado", "producto-eliminado",
                "necesidad", "necesidad-asignacion-pendiente", "necesidad-asignacion-hecha",
                "stock",
                "deposito",
                "asignacion",
                "mision-agregada", "mision-eliminada","donador-set-mision")) {
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