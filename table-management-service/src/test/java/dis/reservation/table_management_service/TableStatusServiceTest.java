package dis.reservation.table_management_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.table_management_service.client.RestaurantServiceClient;
import dis.reservation.table_management_service.dto.RestaurantTableDto;
import dis.reservation.table_management_service.entity.TableStatus;
import dis.reservation.table_management_service.entity.TableStatusEntity;
import dis.reservation.table_management_service.exception.TableManagementGeneralException;
import dis.reservation.table_management_service.repository.TableStatusRepository;
import dis.reservation.table_management_service.service.TableStatusService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableStatusServiceTest {

    private TableStatusRepository tableStatusRepository;
    private RestaurantServiceClient restaurantServiceClient;
    private TableStatusService tableStatusService;

    @BeforeEach
    void setup() {

        tableStatusRepository = mock(TableStatusRepository.class);
        restaurantServiceClient = mock(RestaurantServiceClient.class);
        tableStatusService = new TableStatusService(tableStatusRepository, restaurantServiceClient);
    }

    @Test
    void testCreateStatus_savesNewStatus() {

        RestaurantTableDto dto = RestaurantTableDto.builder()
                .id(1L)
                .number(2)
                .seats(4)
                .build();

        when(restaurantServiceClient.getTableById(1L, 2L)).thenReturn(dto);

        TableStatusEntity savedEntity = TableStatusEntity.builder()
                .restaurantId(1L)
                .tableId(2L)
                .status(TableStatus.AVAILABLE)
                .build();
        when(tableStatusRepository.save(any())).thenReturn(savedEntity);

        TableStatusEntity result = tableStatusService.createStatus(1L, 2L);

        assertEquals(TableStatus.AVAILABLE, result.getStatus());
        assertEquals(2L, result.getTableId());
        verify(tableStatusRepository).save(any());
    }

    @Test
    void testFreeTable_setsSetStatusAvailable() {

        TableStatusEntity status = TableStatusEntity.builder()
                .tableId(2L)
                .status(TableStatus.RESERVED)
                .reservedUntil(LocalDateTime.now().plusMinutes(10))
                .build();
        when(tableStatusRepository.findByTableId(2L)).thenReturn(List.of(status));
        when(tableStatusRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        TableStatusEntity result = tableStatusService.free(2L);

        assertEquals(TableStatus.AVAILABLE, result.getStatus());
        assertNull(result.getReservedUntil());
        verify(tableStatusRepository).save(status);
    }

    @Test
    void testFreeTable_throwsException_whenTableNotFound() {

        when(tableStatusRepository.findByTableId(99L)).thenReturn(List.of());

        assertThrows(TableManagementGeneralException.class,
                     () -> tableStatusService.free(99L));
    }
}

