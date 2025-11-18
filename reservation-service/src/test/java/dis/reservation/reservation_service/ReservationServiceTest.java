package dis.reservation.reservation_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dis.reservation.reservation_service.client.TableServiceClient;
import dis.reservation.reservation_service.dto.CreateReservationRequestDto;
import dis.reservation.reservation_service.dto.TableDto;
import dis.reservation.reservation_service.entity.ReservationEntity;
import dis.reservation.reservation_service.entity.ReservationStatus;
import dis.reservation.reservation_service.exception.ReservationServiceGeneralException;
import dis.reservation.reservation_service.publisher.ReservationPublisher;
import dis.reservation.reservation_service.repository.ReservationRepository;
import dis.reservation.reservation_service.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {

    private TableServiceClient tableServiceClient;
    private ReservationRepository reservationRepository;
    private ReservationPublisher publisher;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {

        tableServiceClient = mock(TableServiceClient.class);
        reservationRepository = mock(ReservationRepository.class);
        publisher = mock(ReservationPublisher.class);

        reservationService = new ReservationService(tableServiceClient, reservationRepository, publisher);
    }

    @Test
    void testCreateReservation_returnsCreatedReservation() {

        CreateReservationRequestDto request = CreateReservationRequestDto.builder()
                .restaurantId(1L)
                .userId(10L)
                .partySize(2)
                .startAt(LocalDateTime.of(2025, 11, 16, 19, 0))
                .durationMinutes(60)
                .build();

        TableDto table = new TableDto(100L, 1L, 4);
        when(tableServiceClient.getAvailableTables(1L, 2)).thenReturn(List.of(table));
        when(reservationRepository.save(any(ReservationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationEntity reservation = reservationService.createReservation(request);

        assertThat(reservation).isNotNull();
        assertThat(reservation.getUserId()).isEqualTo(10L);
        assertThat(reservation.getRestaurantId()).isEqualTo(1L);
        assertThat(reservation.getTableId()).isEqualTo(100L);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CREATED);
        assertThat(reservation.getEndAt()).isEqualTo(request.startAt().plusMinutes(request.durationMinutes()));

        verify(tableServiceClient).reserveTable(eq(1L), eq(100L), 20);
        verify(publisher).publishReservationCreated(any(ReservationEntity.class));
    }

    @Test
    void testCreateReservation_throwsException_whenNoAvailableTables() {

        CreateReservationRequestDto request = CreateReservationRequestDto.builder()
                .restaurantId(1L)
                .userId(10L)
                .partySize(2)
                .startAt(LocalDateTime.now())
                .durationMinutes(60)
                .build();

        when(tableServiceClient.getAvailableTables(1L, 2)).thenReturn(List.of());

        assertThrows(ReservationServiceGeneralException.class,
                     () -> reservationService.createReservation(request));

        verify(tableServiceClient, never()).reserveTable(anyLong(), anyLong(), any());
        verify(publisher, never()).publishReservationCreated(any());
    }
}
