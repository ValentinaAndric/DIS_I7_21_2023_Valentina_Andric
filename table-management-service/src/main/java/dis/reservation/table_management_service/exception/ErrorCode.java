package dis.reservation.table_management_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, Severity.ERROR);

    private final HttpStatus httpStatus;

    private final Severity severity;

    private enum Severity {
        ERROR, WARN, INFO
    }
}