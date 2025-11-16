package dis.reservation.restaurant_service.repository;

import dis.reservation.restaurant_service.entity.TableEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<TableEntity, Long> {

    List<TableEntity> findByRestaurantId(Long restaurantId);
}
