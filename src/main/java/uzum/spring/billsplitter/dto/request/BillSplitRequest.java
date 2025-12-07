package uzum.spring.billsplitter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillSplitRequest {

    @NotEmpty
    @Valid
    private List<PersonOrderDto> people;

    @NotNull
    @Valid
    private List<ItemDto> sharedItems;

    @NotNull
    @PositiveOrZero
    private BigDecimal commissionPercent;
}