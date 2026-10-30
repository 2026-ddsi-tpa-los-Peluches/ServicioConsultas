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
        for (String nombre : List.of("donador-estado", "donador-incentivos", "donacion",
                "necesidad", "stock", "deposito", "asignacion")) {
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