package dis.reservation.restaurant_service.service;

import dis.reservation.restaurant_service.dto.RestaurantTableDto;
import dis.reservation.restaurant_service.entity.RestaurantEntity;
import dis.reservation.restaurant_service.entity.TableEntity;
import dis.reservation.restaurant_service.exception.ErrorCode;
import dis.reservation.restaurant_service.exception.RestaurantServiceGeneralException;
import dis.reservation.restaurant_service.repository.RestaurantRepository;
import dis.reservation.restaurant_service.repository.TableRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    public TableService(TableRepository tableRepository,
            RestaurantRepository restaurantRepository) {

        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<TableEntity> getTablesByRestaurant(Long restaurantId) {

        return tableRepository.findByRestaurantId(restaurantId);
    }

    public TableEntity createTable(Long restaurantId, TableEntity table) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new RestaurantServiceGeneralException("Restaurant not found with id: " + restaurantId,
                                                                    ErrorCode.INTERNAL_SERVER_ERROR));

        table.setRestaurant(restaurant);
        return tableRepository.save(table);
    }

    public List<TableEntity> getAllTables() {

        return tableRepository.findAll();
    }

    public TableEntity getTableById(Long id) {

        return tableRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Table not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));
    }

    public TableEntity updateTable(Long id, TableEntity updated) {

        TableEntity existing = tableRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Table not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));

        existing.setNumber(updated.getNumber());
        existing.setSeats(updated.getSeats());

        if (updated.getRestaurant() != null) {
            existing.setRestaurant(updated.getRestaurant());
        }

        return tableRepository.save(existing);
    }

    public RestaurantTableDto getTableForRestaurant(Long restaurantId, Long tableId) {

        TableEntity table = getTableById(tableId);

        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new RestaurantServiceGeneralException("Table does not belong to restaurant",
                                                        ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return new RestaurantTableDto(
                table.getId(),
                table.getNumber(),
                table.getSeats(),
                table.getRestaurant().getId()
        );
    }

    public void deleteTable(Long id) {

        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Table not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));

        tableRepository.delete(table);
    }
}

