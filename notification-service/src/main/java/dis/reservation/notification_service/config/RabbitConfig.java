package dis.reservation.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "reservations.exchange";
    public static final String QUEUE = "reservation.created.queue";
    public static final String ROUTING_KEY = "reservation.created";

    @Bean
    public TopicExchange reservationExchange() {

        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {

        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding bindCreated() {

        return BindingBuilder.bind(createdQueue())
                .to(reservationExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}

