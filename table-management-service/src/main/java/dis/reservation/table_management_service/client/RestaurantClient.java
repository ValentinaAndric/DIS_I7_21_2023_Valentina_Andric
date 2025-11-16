package dis.reservation.table_management_service.client;

import dis.reservation.table_management_service.dto.RestaurantTableDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/tables/{restaurantId}/{tableId}")
    RestaurantTableDto getTableById(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId
    );

    @GetMapping("/api/tables/restaurant/{restaurantId}")
    List<RestaurantTableDto> getTablesByRestaurant(@PathVariable Long restaurantId);

}

