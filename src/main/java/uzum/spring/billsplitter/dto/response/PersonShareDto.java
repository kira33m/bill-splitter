package uzum.spring.billsplitter.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PersonShareDto(
    String name,
    BigDecimal personalTotal,
    BigDecimal sharedTotal,
    BigDecimal commission,
    BigDecimal totalToPay
) {
}