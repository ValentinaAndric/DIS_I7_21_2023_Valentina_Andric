package dis.reservation.notification_service.event;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationCreatedEvent {
    private Long reservationId;
    private Long userId;
    private Long restaurantId;
    private Long tableId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}

