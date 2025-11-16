package dis.reservation.reservation_service.publisher;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ReservationCreatedEvent(Long reservationId, Long userId, Long restaurantId, Long tableId,
                                      LocalDateTime startAt,
                                      LocalDateTime endAt) implements Serializable {

}
