package uzum.spring.billsplitter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponseDto(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ValidationError> validationErrors
) {
    public ErrorResponseDto {
        validationErrors = validationErrors == null ? List.of() : List.copyOf(validationErrors);
    }

    @Builder
    public record ValidationError(
        String field,
        String message
    ) {
    }
}