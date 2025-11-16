package dis.reservation.restaurant_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.restaurant_service.entity.RestaurantEntity;
import dis.reservation.restaurant_service.entity.TableEntity;
import dis.reservation.restaurant_service.exception.RestaurantServiceGeneralException;
import dis.reservation.restaurant_service.repository.RestaurantRepository;
import dis.reservation.restaurant_service.repository.TableRepository;
import dis.reservation.restaurant_service.service.TableService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableServiceTest {

    private TableRepository tableRepository;
    private RestaurantRepository restaurantRepository;
    private TableService tableService;

    @BeforeEach
    void setUp() {

        tableRepository = mock(TableRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        tableService = new TableService(tableRepository, restaurantRepository);
    }

    @Test
    void testGetTablesByRestaurant() {

        TableEntity table = new TableEntity();
        table.setId(1L);

        when(tableRepository.findByRestaurantId(1L))
                .thenReturn(List.of(table));

        List<TableEntity> result = tableService.getTablesByRestaurant(1L);

        assertEquals(1, result.size());
        verify(tableRepository).findByRestaurantId(1L);
    }

    @Test
    void testCreateTable_Success() {

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(10L);

        TableEntity table = new TableEntity();
        table.setNumber(5);

        when(restaurantRepository.findById(10L))
                .thenReturn(Optional.of(restaurant));
        when(tableRepository.save(table))
                .thenReturn(table);

        TableEntity created = tableService.createTable(10L, table);

        assertEquals(restaurant, created.getRestaurant());
        verify(tableRepository).save(table);
    }

    @Test
    void testCreateTable_RestaurantNotFound() {

        when(restaurantRepository.findById(10L))
                .thenReturn(Optional.empty());

        TableEntity table = new TableEntity();

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> tableService.createTable(10L, table));
    }

    @Test
    void testGetAllTables() {

        when(tableRepository.findAll())
                .thenReturn(List.of(new TableEntity()));

        List<TableEntity> tables = tableService.getAllTables();

        assertEquals(1, tables.size());
    }

    @Test
    void testGetTableById_Success() {

        TableEntity table = new TableEntity();
        table.setId(3L);

        when(tableRepository.findById(3L))
                .thenReturn(Optional.of(table));

        TableEntity result = tableService.getTableById(3L);

        assertEquals(3L, result.getId());
    }

    @Test
    void testGetTableById_NotFound() {

        when(tableRepository.findById(3L))
                .thenReturn(Optional.empty());

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> tableService.getTableById(3L));
    }

    @Test
    void testUpdateTable_Success() {

        TableEntity existing = new TableEntity();
        existing.setId(5L);
        existing.setNumber(1);
        existing.setSeats(2);

        TableEntity update = new TableEntity();
        update.setNumber(10);
        update.setSeats(4);

        when(tableRepository.findById(5L))
                .thenReturn(Optional.of(existing));
        when(tableRepository.save(existing))
                .thenReturn(existing);

        TableEntity result = tableService.updateTable(5L, update);

        assertEquals(10, result.getNumber());
        assertEquals(4, result.getSeats());
        verify(tableRepository).save(existing);
    }

    @Test
    void testUpdateTable_NotFound() {

        when(tableRepository.findById(5L))
                .thenReturn(Optional.empty());

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> tableService.updateTable(5L, new TableEntity()));
    }

    @Test
    void testDeleteTable_Success() {

        TableEntity table = new TableEntity();
        table.setId(2L);

        when(tableRepository.findById(2L))
                .thenReturn(Optional.of(table));

        tableService.deleteTable(2L);

        verify(tableRepository).delete(table);
    }

    @Test
    void testDeleteTable_NotFound() {

        when(tableRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> tableService.deleteTable(2L));
    }
}

