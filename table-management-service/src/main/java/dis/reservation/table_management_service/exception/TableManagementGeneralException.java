package dis.reservation.table_management_service.exception;

import lombok.Getter;

@Getter
public class TableManagementGeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public TableManagementGeneralException(String message, ErrorCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
}
