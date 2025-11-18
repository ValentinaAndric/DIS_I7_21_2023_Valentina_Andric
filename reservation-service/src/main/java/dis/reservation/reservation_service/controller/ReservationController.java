package dis.reservation.reservation_service.controller;

import dis.reservation.reservation_service.dto.CreateReservationRequestDto;
import dis.reservation.reservation_service.entity.ReservationEntity;
import dis.reservation.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationEntity> create(@RequestBody CreateReservationRequestDto request) {

        ReservationEntity reservationEntity = reservationService.createReservation(request);
        return ResponseEntity.status(201).body(reservationEntity);
    }
}
