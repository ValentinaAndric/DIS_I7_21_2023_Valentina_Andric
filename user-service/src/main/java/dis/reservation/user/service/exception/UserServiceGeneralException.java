package dis.reservation.user.service.exception;

import lombok.Getter;

@Getter
public class UserServiceGeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public UserServiceGeneralException(String message, ErrorCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
}
