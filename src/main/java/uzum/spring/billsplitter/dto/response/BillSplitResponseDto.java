package uzum.spring.billsplitter.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BillSplitResponseDto(
    List<PersonShareDto> shares,
    BigDecimal totalWithoutCommission,
    BigDecimal totalCommission,
    BigDecimal totalWithCommission
) {
    public BillSplitResponseDto {
        shares = shares == null ? List.of() : List.copyOf(shares);
    }
}