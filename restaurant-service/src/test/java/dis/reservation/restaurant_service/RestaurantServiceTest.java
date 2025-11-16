package dis.reservation.restaurant_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.restaurant_service.entity.RestaurantEntity;
import dis.reservation.restaurant_service.exception.RestaurantServiceGeneralException;
import dis.reservation.restaurant_service.repository.RestaurantRepository;
import dis.reservation.restaurant_service.service.RestaurantService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantServiceTest {

    private RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {

        restaurantRepository = mock(RestaurantRepository.class);
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void testGetRestaurantById_Found() {

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantEntity result = restaurantService.getRestaurantById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Restaurant", result.getName());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantById_NotFound() {

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> restaurantService.getRestaurantById(1L));
    }

    @Test
    void testCreateRestaurant() {

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setName("Pizza Place");

        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        RestaurantEntity saved = restaurantService.createRestaurant(restaurant);

        assertEquals("Pizza Place", saved.getName());
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testDeleteRestaurant() {

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(5L);

        when(restaurantRepository.findById(5L)).thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(5L);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void testDeleteRestaurant_NotFound() {

        when(restaurantRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RestaurantServiceGeneralException.class,
                     () -> restaurantService.deleteRestaurant(10L));
    }
}
