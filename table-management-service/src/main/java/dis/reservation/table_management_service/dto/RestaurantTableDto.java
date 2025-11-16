package dis.reservation.table_management_service.dto;

import lombok.Builder;

@Builder
public record RestaurantTableDto(
        Long id,
        int number,
        int seats,
        Long restaurantId
) {

}
