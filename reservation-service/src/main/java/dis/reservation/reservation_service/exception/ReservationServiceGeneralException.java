package dis.reservation.reservation_service.exception;

import lombok.Getter;

@Getter
public class ReservationServiceGeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public ReservationServiceGeneralException(String message, ErrorCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
}
