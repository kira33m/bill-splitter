package uzum.spring.billsplitter.exception;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import uzum.spring.billsplitter.constant.enums.ErrorType;

@Getter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class BusinessException extends RuntimeException {

    HttpStatus status;
    ErrorType errorType;

    public BusinessException(String message, ErrorType errorType, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorType = errorType;
    }
}
