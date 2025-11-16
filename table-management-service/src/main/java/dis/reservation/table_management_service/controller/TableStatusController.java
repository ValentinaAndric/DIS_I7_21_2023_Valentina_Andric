package dis.reservation.table_management_service.controller;

import dis.reservation.table_management_service.dto.RestaurantTableDto;
import dis.reservation.table_management_service.entity.TableStatusEntity;
import dis.reservation.table_management_service.service.TableStatusService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-status")
@RequiredArgsConstructor
public class TableStatusController {

    private final TableStatusService tableStatusService;

    @PostMapping("/{restaurantId}/{tableId}")
    public TableStatusEntity createTableStatus(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId
    ) {

        return tableStatusService.createStatus(restaurantId, tableId);
    }

    @PostMapping("/reserve/{restaurantId}/{tableId}")
    public TableStatusEntity reserveTable(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId,
            @RequestParam(defaultValue = "60") int minutes
    ) {

        return tableStatusService.reserve(restaurantId, tableId, minutes);
    }

    @PostMapping("/free/{tableId}")
    public TableStatusEntity freeTable(@PathVariable Long tableId) {

        return tableStatusService.free(tableId);
    }

    @GetMapping("/{restaurantId}/available")
    public ResponseEntity<List<RestaurantTableDto>> getAvailableTables(
            @PathVariable Long restaurantId,
            @RequestParam int partySize) {

        List<RestaurantTableDto> tables = tableStatusService.getAvailableTables(restaurantId, partySize);
        return ResponseEntity.ok(tables);
    }
}

