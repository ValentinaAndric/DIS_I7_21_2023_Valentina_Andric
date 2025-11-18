package dis.reservation.restaurant_service;

import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_ADDRESS_FOR_DELETE;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_NAME_FOR_DELETE;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_NEW_ADDRESS;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_NEW_NAME;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_OLD_ADDRESS;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.RESTAURANT_OLD_NAME;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.TEST_RESTAURANT;
import static dis.reservation.restaurant_service.RestaurantServiceDataFixture.TEST_RESTAURANT_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dis.reservation.restaurant_service.entity.RestaurantEntity;
import dis.reservation.restaurant_service.repository.RestaurantRepository;
import dis.reservation.restaurant_service.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RestaurantServiceIntegrationTest implements PostgresTestContainer {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void cleanDb() {

        restaurantRepository.deleteAll();
    }

    @Test
    void testShouldSaveNewRestaurant_whenNewRestaurantAdded() {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(TEST_RESTAURANT)
                .address(TEST_RESTAURANT_ADDRESS)
                .build();

        RestaurantEntity newRestaurant = restaurantService.createRestaurant(restaurant);

        assertNotNull(newRestaurant.getId());

        RestaurantEntity savedEntity = restaurantRepository.findById(newRestaurant.getId()).orElseThrow();
        assertEquals(TEST_RESTAURANT, savedEntity.getName());
        assertEquals(TEST_RESTAURANT_ADDRESS, savedEntity.getAddress());
    }

    @Test
    void testShouldReturnRestaurantById_whenExistingRestaurantPassed() {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(TEST_RESTAURANT)
                .address(TEST_RESTAURANT_ADDRESS)
                .build();

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        RestaurantEntity foundRestaurant = restaurantService.getRestaurantById(savedRestaurant.getId());

        assertEquals(TEST_RESTAURANT, foundRestaurant.getName());
    }

    @Test
    void testShouldUpdateRestaurant_WhenUpdatedRestaurantPassed() {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(RESTAURANT_OLD_NAME)
                .address(RESTAURANT_OLD_ADDRESS)
                .build();

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        RestaurantEntity updateRestaurant = RestaurantEntity.builder()
                .name(RESTAURANT_NEW_NAME)
                .address(RESTAURANT_NEW_ADDRESS)
                .build();

        RestaurantEntity updatedRestaurant = restaurantService.putRestaurant(savedRestaurant.getId(), updateRestaurant);

        assertEquals(RESTAURANT_NEW_NAME, updatedRestaurant.getName());
        assertEquals(RESTAURANT_NEW_ADDRESS, updatedRestaurant.getAddress());

        RestaurantEntity foundedRestaurant = restaurantRepository.findById(savedRestaurant.getId()).orElseThrow();
        assertEquals(RESTAURANT_NEW_NAME, foundedRestaurant.getName());
    }

    @Test
    void testShouldDeleteEntity_whenDeletedRestaurantPassed() {

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(RESTAURANT_NAME_FOR_DELETE)
                .address(RESTAURANT_ADDRESS_FOR_DELETE)
                .build();

        RestaurantEntity saved = restaurantRepository.save(restaurant);

        restaurantService.deleteRestaurant(saved.getId());

        assertFalse(restaurantRepository.findById(saved.getId()).isPresent());
    }
}

