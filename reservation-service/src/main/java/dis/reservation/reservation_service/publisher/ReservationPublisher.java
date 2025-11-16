package dis.reservation.reservation_service.publisher;

import dis.reservation.reservation_service.entity.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationPublisher {

    private final AmqpTemplate amqpTemplate;

    public void publishReservationCreated(ReservationEntity reservation) {

        var event = new ReservationCreatedEvent(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getRestaurantId(),
                reservation.getTableId(),
                reservation.getStartAt(),
                reservation.getEndAt()
        );

        amqpTemplate.convertAndSend(
                "reservations.exchange",
                "reservation.created",
                event
        );
    }
}

