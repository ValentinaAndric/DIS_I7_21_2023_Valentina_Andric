package dis.reservation.reservation_service.client;

import dis.reservation.reservation_service.dto.ReserveRequestDto;
import dis.reservation.reservation_service.dto.TableDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "table-management-service")
public interface TableClient {

    @GetMapping("/api/table-status/{restaurantId}/available")
    List<TableDto> findAvailable(@PathVariable Long restaurantId,
            @RequestParam int partySize);

    @PostMapping("/api/table-status/reserve/{restaurantId}/{tableId}")
    ResponseEntity<Void> reserve(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId,
            @RequestBody ReserveRequestDto request
    );
}

