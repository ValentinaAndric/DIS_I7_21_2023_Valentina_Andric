package dis.reservation.reservation_service.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ReservationServiceGeneralException.class)
    protected ProblemDetail handleRecommenderGeneralException(ReservationServiceGeneralException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getErrorCode().getHttpStatus(),
                                                                       e.getMessage());
        log.error("{} {}", e.getMessage(), ExceptionUtils.getStackTrace(e));
        return problemDetail;
    }

}