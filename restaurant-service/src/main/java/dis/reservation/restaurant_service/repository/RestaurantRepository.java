package dis.reservation.restaurant_service.repository;

import dis.reservation.restaurant_service.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

}
