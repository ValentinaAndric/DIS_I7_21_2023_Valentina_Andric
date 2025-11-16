package dis.reservation.restaurant_service.dto;

public record RestaurantTableDto(
        Long id,
        int number,
        int seats,
        Long restaurantId
) {

}
