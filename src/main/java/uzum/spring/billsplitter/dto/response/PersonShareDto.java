package uzum.spring.billsplitter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonShareDto {

    private String name;
    private BigDecimal personalTotal;
    private BigDecimal sharedTotal;
    private BigDecimal commission;
    private BigDecimal totalToPay;
}