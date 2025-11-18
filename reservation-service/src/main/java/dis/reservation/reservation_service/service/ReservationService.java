package dis.reservation.reservation_service.service;

import dis.reservation.reservation_service.client.TableServiceClient;
import dis.reservation.reservation_service.dto.CreateReservationRequestDto;
import dis.reservation.reservation_service.entity.ReservationEntity;
import dis.reservation.reservation_service.entity.ReservationStatus;
import dis.reservation.reservation_service.exception.ErrorCode;
import dis.reservation.reservation_service.exception.ReservationServiceGeneralException;
import dis.reservation.reservation_service.publisher.ReservationPublisher;
import dis.reservation.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TableServiceClient tableServiceClient;
    private final ReservationRepository reservationRepository;
    private final ReservationPublisher publisher;

    @Transactional
    public ReservationEntity createReservation(CreateReservationRequestDto req) {

        var tables = tableServiceClient.getAvailableTables(req.restaurantId(), req.partySize());
        if (tables.isEmpty()) {
            throw new ReservationServiceGeneralException("No available tables", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        var table = tables.get(0);

        tableServiceClient.reserveTable(req.restaurantId(), table.id(), req.durationMinutes());

        ReservationEntity reservation = ReservationEntity.builder()
                .userId(req.userId())
                .restaurantId(req.restaurantId())
                .tableId(table.id())
                .startAt(req.startAt())
                .endAt(req.startAt().plusMinutes(req.durationMinutes()))
                .status(ReservationStatus.CREATED)
                .build();

        reservationRepository.save(reservation);

        publisher.publishReservationCreated(reservation);

        return reservation;
    }
}


