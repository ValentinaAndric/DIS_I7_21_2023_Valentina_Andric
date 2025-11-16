package dis.reservation.reservation_service.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateReservationRequestDto(
        Long restaurantId,
        Long userId,
        int partySize,
        LocalDateTime startAt,
        int durationMinutes
) {

}
