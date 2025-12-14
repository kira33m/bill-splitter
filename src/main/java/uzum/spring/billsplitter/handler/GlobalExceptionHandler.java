package uzum.spring.billsplitter.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uzum.spring.billsplitter.dto.response.ErrorResponseDto;
import uzum.spring.billsplitter.exception.BusinessException;
import uzum.spring.billsplitter.exception.EmptyPeopleListException;
import uzum.spring.billsplitter.exception.InvalidCommissionException;
import uzum.spring.billsplitter.exception.NegativePriceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request) {

        log.error("Validation error: {}", ex.getMessage());

        List<ErrorResponseDto.ValidationError> validationErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.add(ErrorResponseDto.ValidationError.builder()
                .field(fieldName)
                .message(errorMessage)
                .build());
        });

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Validation failed")
            .path(request.getRequestURI())
            .validationErrors(validationErrors)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(
        BusinessException ex,
        HttpServletRequest request) {

        log.error("Business exception: {} - Type: {}", ex.getMessage(), ex.getErrorType());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            ex.getStatus(),
            request.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(EmptyPeopleListException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyPeopleListException(
        EmptyPeopleListException ex,
        HttpServletRequest request) {

        log.error("Empty people list error: {}", ex.getMessage());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidCommissionException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCommissionException(
        InvalidCommissionException ex,
        HttpServletRequest request) {

        log.error("Invalid commission error: {}", ex.getMessage());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NegativePriceException.class)
    public ResponseEntity<ErrorResponseDto> handleNegativePriceException(
        NegativePriceException ex,
        HttpServletRequest request) {

        log.error("Negative price error: {}", ex.getMessage());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
        ConstraintViolationException ex,
        HttpServletRequest request) {

        log.error("Constraint violation: {}", ex.getMessage());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
        IllegalArgumentException ex,
        HttpServletRequest request) {

        log.error("Illegal argument error: {}", ex.getMessage());

        ErrorResponseDto errorResponse = buildErrorResponse(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
        Exception ex,
        HttpServletRequest request) {

        log.error("Unexpected error occurred: ", ex);

        ErrorResponseDto errorResponse = buildErrorResponse(
            "An unexpected error occurred. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponseDto buildErrorResponse(String message, HttpStatus status, String path) {
        return ErrorResponseDto.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(message)
            .path(path)
            .build();
    }
}