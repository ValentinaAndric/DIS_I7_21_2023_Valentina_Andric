package dis.reservation.restaurant_service.exception;

import lombok.Getter;

@Getter
public class RestaurantServiceGeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public RestaurantServiceGeneralException(String message, ErrorCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
}
