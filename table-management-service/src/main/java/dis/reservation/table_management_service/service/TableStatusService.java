package dis.reservation.table_management_service.service;

import dis.reservation.table_management_service.client.RestaurantServiceClient;
import dis.reservation.table_management_service.dto.RestaurantTableDto;
import dis.reservation.table_management_service.entity.TableStatus;
import dis.reservation.table_management_service.entity.TableStatusEntity;
import dis.reservation.table_management_service.exception.ErrorCode;
import dis.reservation.table_management_service.exception.TableManagementGeneralException;
import dis.reservation.table_management_service.repository.TableStatusRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableStatusService {

    private final TableStatusRepository tableStatusRepository;
    private final RestaurantServiceClient restaurantServiceClient;

    public TableStatusEntity createStatus(Long restaurantId, Long tableId) {

        RestaurantTableDto tableDTO = restaurantServiceClient.getTableById(restaurantId, tableId);

        TableStatusEntity status = TableStatusEntity.builder()
                .restaurantId(tableDTO.restaurantId())
                .tableId(tableDTO.id())
                .status(TableStatus.AVAILABLE)
                .reservedUntil(null)
                .build();

        return tableStatusRepository.save(status);
    }

    public TableStatusEntity reserve(Long restaurantId, Long tableId, int minutes) {

        RestaurantTableDto tableDTO = restaurantServiceClient.getTableById(restaurantId, tableId);

        Optional<TableStatusEntity> existing = tableStatusRepository
                .findByTableId(tableDTO.id())
                .stream()
                .findFirst();

        TableStatusEntity status = existing.orElseGet(() ->
                                                              TableStatusEntity.builder()
                                                                      .restaurantId(tableDTO.restaurantId())
                                                                      .tableId(tableDTO.id())
                                                                      .build()
        );

        status.setStatus(TableStatus.RESERVED);
        status.setReservedUntil(LocalDateTime.now().plusMinutes(minutes));

        return tableStatusRepository.save(status);
    }

    public TableStatusEntity free(Long tableId) {

        TableStatusEntity status = tableStatusRepository.findByTableId(tableId)
                .stream().findFirst()
                .orElseThrow(
                        () -> new TableManagementGeneralException("Table not found", ErrorCode.INTERNAL_SERVER_ERROR));

        status.setStatus(TableStatus.AVAILABLE);
        status.setReservedUntil(null);

        return tableStatusRepository.save(status);
    }

    public List<RestaurantTableDto> getAvailableTables(Long restaurantId, int partySize) {

        List<RestaurantTableDto> allTables = restaurantServiceClient.getTablesByRestaurant(restaurantId);

        List<Long> suitableTableIds = allTables.stream()
                .filter(t -> t.seats() >= partySize)
                .map(RestaurantTableDto::id)
                .toList();

        if (suitableTableIds.isEmpty()) {
            return List.of();
        }

        List<TableStatusEntity> availableStatuses = tableStatusRepository
                .findByRestaurantIdAndStatusAndTableIdIn(restaurantId, TableStatus.AVAILABLE, suitableTableIds);

        return allTables.stream()
                .filter(t -> availableStatuses.stream().anyMatch(s -> s.getTableId().equals(t.id())))
                .toList();
    }

}

