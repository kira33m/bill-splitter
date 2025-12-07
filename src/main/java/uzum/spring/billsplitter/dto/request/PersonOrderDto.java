package uzum.spring.billsplitter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonOrderDto {

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private List<ItemDto> personalItems;
}