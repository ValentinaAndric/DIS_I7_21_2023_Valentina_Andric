package dis.reservation.restaurant_service.service;

import dis.reservation.restaurant_service.entity.RestaurantEntity;
import dis.reservation.restaurant_service.exception.ErrorCode;
import dis.reservation.restaurant_service.exception.RestaurantServiceGeneralException;
import dis.reservation.restaurant_service.repository.RestaurantRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {

        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantEntity> getAllRestaurants() {

        return restaurantRepository.findAll();
    }

    public RestaurantEntity createRestaurant(RestaurantEntity restaurant) {

        return restaurantRepository.save(restaurant);
    }

    public RestaurantEntity getRestaurantById(Long id) {

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Restaurant not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));
    }

    public RestaurantEntity putRestaurant(Long id, RestaurantEntity restaurant) {

        RestaurantEntity existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Restaurant not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setAddress(restaurant.getAddress());

        existingRestaurant.setTables(restaurant.getTables());

        return restaurantRepository.save(existingRestaurant);
    }

    public void deleteRestaurant(Long id) {

        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantServiceGeneralException("Restaurant not found with id: " + id,
                                                                         ErrorCode.INTERNAL_SERVER_ERROR));

        restaurantRepository.delete(restaurant);
    }

}
