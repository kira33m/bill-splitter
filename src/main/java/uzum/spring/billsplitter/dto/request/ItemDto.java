package uzum.spring.billsplitter.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemDto(

    @NotBlank(message = "Item name cannot be blank")
    @Size(min = 1, max = 200, message = "Item name must be between 1 and 200 characters")
    String name,

    @NotNull(message = "Item price cannot be null")
    @DecimalMin(value = "0.0", message = "Item price must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "Item price must have at most 10 integer digits and 2 fractional digits")
    BigDecimal price
) {
}