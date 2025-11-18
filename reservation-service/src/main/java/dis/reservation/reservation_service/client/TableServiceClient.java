package dis.reservation.reservation_service.client;

import dis.reservation.reservation_service.dto.ReserveRequestDto;
import dis.reservation.reservation_service.dto.TableDto;
import dis.reservation.reservation_service.exception.ErrorCode;
import dis.reservation.reservation_service.exception.ReservationServiceGeneralException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableServiceClient {

    private final TableClient tableClient;

    @CircuitBreaker(name = "tableFindCB", fallbackMethod = "fallbackTables")
    public List<TableDto> getAvailableTables(Long restaurantId, int partySize) {

        return tableClient.findAvailable(restaurantId, partySize);
    }

    @CircuitBreaker(name = "tableReserveCB", fallbackMethod = "fallbackReserve")
    public void reserveTable(Long restaurantId, Long tableId, int duration) {

        tableClient.reserve(restaurantId, tableId, new ReserveRequestDto(duration));
    }

    public List<?> fallbackTables(Long restaurantId, int partySize, Throwable ex) {

        throw new ReservationServiceGeneralException(
                "Table service unavailable (find)", ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public void fallbackReserve(Long restaurantId, Long tableId, int duration, Throwable ex) {

        throw new ReservationServiceGeneralException(
                "Table service unavailable (reserve)", ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
