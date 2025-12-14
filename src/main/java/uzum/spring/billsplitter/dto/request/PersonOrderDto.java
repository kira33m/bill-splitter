package uzum.spring.billsplitter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record PersonOrderDto(
    @NotBlank(message = "Person name cannot be blank")
    @Size(min = 1, max = 100, message = "Person name must be between 1 and 100 characters")
    String name,

    @NotNull(message = "Personal items list cannot be null")
    @Valid
    List<ItemDto> personalItems
) {
    public PersonOrderDto {
        personalItems = personalItems != null ? List.copyOf(personalItems) : null;
    }
}