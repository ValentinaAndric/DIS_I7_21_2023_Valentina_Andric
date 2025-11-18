package dis.reservation.table_management_service.client;

import dis.reservation.table_management_service.dto.RestaurantTableDto;
import dis.reservation.table_management_service.exception.ErrorCode;
import dis.reservation.table_management_service.exception.TableManagementGeneralException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceClient {

    private final RestaurantClient restaurantClient;

    private static final String RESTAURANT_SERVICE_CB = "restaurantServiceCircuitBreaker";

    @CircuitBreaker(name = RESTAURANT_SERVICE_CB, fallbackMethod = "getTableByIdFallback")
    public RestaurantTableDto getTableById(Long restaurantId, Long tableId) {

        return restaurantClient.getTableById(restaurantId, tableId);
    }

    @CircuitBreaker(name = RESTAURANT_SERVICE_CB, fallbackMethod = "getTablesByRestaurantFallback")
    public List<RestaurantTableDto> getTablesByRestaurant(Long restaurantId) {

        return restaurantClient.getTablesByRestaurant(restaurantId);
    }

    private RestaurantTableDto getTableByIdFallback(Long restaurantId, Long tableId, Throwable throwable) {

        throw new TableManagementGeneralException("Restaurant service unavailable now",
                                                  ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private List<RestaurantTableDto> getTablesByRestaurantFallback(Long restaurantId, Throwable throwable) {

        throw new TableManagementGeneralException("Restaurant service unavailable now",
                                                  ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

