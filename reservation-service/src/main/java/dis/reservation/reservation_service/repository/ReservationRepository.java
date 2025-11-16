package dis.reservation.reservation_service.repository;

import dis.reservation.reservation_service.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

}
