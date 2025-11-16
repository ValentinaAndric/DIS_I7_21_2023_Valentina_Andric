package dis.reservation.reservation_service.service;

import dis.reservation.reservation_service.client.TableClient;
import dis.reservation.reservation_service.config.UserDetailsImpl;
import dis.reservation.reservation_service.dto.CreateReservationRequestDto;
import dis.reservation.reservation_service.dto.ReserveRequestDto;
import dis.reservation.reservation_service.entity.ReservationEntity;
import dis.reservation.reservation_service.entity.ReservationStatus;
import dis.reservation.reservation_service.exception.ErrorCode;
import dis.reservation.reservation_service.exception.ReservationServiceGeneralException;
import dis.reservation.reservation_service.publisher.ReservationPublisher;
import dis.reservation.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TableClient tableClient;
    private final ReservationRepository reservationRepository;
    private final ReservationPublisher publisher;

    @Transactional
    public ReservationEntity createReservation(CreateReservationRequestDto req) {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
//        Long userId = userDetails.getId();

        var tables = tableClient.findAvailable(req.restaurantId(), req.partySize());
        if (tables.isEmpty()) {
            throw new ReservationServiceGeneralException("No available tables", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        var table = tables.get(0);

        tableClient.reserve(req.restaurantId(), table.id(), new ReserveRequestDto(req.durationMinutes()));

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


