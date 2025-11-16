package dis.reservation.table_management_service.repository;

import dis.reservation.table_management_service.entity.TableStatus;
import dis.reservation.table_management_service.entity.TableStatusEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableStatusRepository extends JpaRepository<TableStatusEntity, Long> {

    List<TableStatusEntity> findByTableId(Long tableId);

    List<TableStatusEntity> findByRestaurantIdAndStatus(Long restaurantId, TableStatus status);

    List<TableStatusEntity> findByRestaurantIdAndStatusAndTableIdIn(Long restaurantId, TableStatus status,
            List<Long> tableIds);
}
