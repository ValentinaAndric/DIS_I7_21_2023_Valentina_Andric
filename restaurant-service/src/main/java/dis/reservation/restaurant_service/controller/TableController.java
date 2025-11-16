package dis.reservation.restaurant_service.controller;

import dis.reservation.restaurant_service.dto.RestaurantTableDto;
import dis.reservation.restaurant_service.entity.TableEntity;
import dis.reservation.restaurant_service.service.TableService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {

        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<TableEntity>> getAllTables() {

        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<TableEntity>> getTablesByRestaurant(@PathVariable Long restaurantId) {

        List<TableEntity> tables = tableService.getTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<TableEntity> createTable(
            @PathVariable Long restaurantId,
            @RequestBody TableEntity tableRequest) {

        TableEntity created = tableService.createTable(restaurantId, tableRequest);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableEntity> getTableById(@PathVariable Long id) {

        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableEntity> updateTable(
            @PathVariable Long id,
            @RequestBody TableEntity tableRequest) {

        TableEntity updated = tableService.updateTable(id, tableRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {

        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{restaurantId}/{tableId}")
    public ResponseEntity<RestaurantTableDto> getTableForRestaurant(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId) {

        TableEntity table = tableService.getTableById(tableId);

        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Table does not belong to restaurant");
        }

        RestaurantTableDto dto = new RestaurantTableDto(
                table.getId(),
                table.getNumber(),
                table.getSeats(),
                table.getRestaurant().getId()
        );

        return ResponseEntity.ok(dto);
    }
}

