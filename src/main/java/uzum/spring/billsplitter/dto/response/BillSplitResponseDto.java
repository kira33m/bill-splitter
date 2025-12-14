package uzum.spring.billsplitter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillSplitResponseDto {

    private List<PersonShareDto> shares;

    private BigDecimal totalWithoutCommission;
    private BigDecimal totalCommission;
    private BigDecimal totalWithCommission;
}