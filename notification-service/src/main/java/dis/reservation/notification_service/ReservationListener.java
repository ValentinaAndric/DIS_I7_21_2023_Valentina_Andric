package dis.reservation.notification_service;

import dis.reservation.notification_service.event.ReservationCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationListener {

    @RabbitListener(queues = "reservation.created.queue")
    public void handle(ReservationCreatedEvent event) {

        log.info("Received notification: reservation {} for user {}",
                 event.getReservationId(), event.getUserId());
    }
}

