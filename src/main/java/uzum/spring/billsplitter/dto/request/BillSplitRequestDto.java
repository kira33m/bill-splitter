package uzum.spring.billsplitter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BillSplitRequestDto(
    @NotNull(message = "People list cannot be null")
    @Size(min = 1, message = "At least one person is required")
    @Valid
    List<PersonOrderDto> people,

    @NotNull(message = "Shared items list cannot be null")
    @Valid
    List<ItemDto> sharedItems,

    @NotNull(message = "Commission percent cannot be null")
    @DecimalMin(value = "0.0", message = "Commission percent must be greater than or equal to 0")
    @DecimalMax(value = "100.0", message = "Commission percent must be less than or equal to 100")
    BigDecimal commissionPercent
) {
    public BillSplitRequestDto {
        people = people != null ? List.copyOf(people) : null;
        sharedItems = sharedItems != null ? List.copyOf(sharedItems) : null;
    }
}