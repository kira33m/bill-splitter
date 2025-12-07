package uzum.spring.billsplitter.service.impl;

import uzum.spring.billsplitter.dto.request.BillSplitRequest;
import uzum.spring.billsplitter.dto.request.ItemDto;
import uzum.spring.billsplitter.dto.request.PersonOrderDto;
import uzum.spring.billsplitter.dto.response.BillSplitResponse;
import uzum.spring.billsplitter.dto.response.PersonShareDto;
import uzum.spring.billsplitter.service.BillSplitService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillSplitServiceImpl implements BillSplitService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Override
    public BillSplitResponse splitBill(BillSplitRequest request) {
        if (request.getPeople() == null || request.getPeople().isEmpty()) {
            throw new IllegalArgumentException("List of people must not be empty");
        }

        int peopleCount = request.getPeople().size();

        BigDecimal sharedTotal = sumItems(request.getSharedItems());
        BigDecimal sharedPerPerson = peopleCount > 0
            ? sharedTotal.divide(BigDecimal.valueOf(peopleCount), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        List<PersonShareDto> shares = new ArrayList<>();
        BigDecimal totalWithoutCommission = BigDecimal.ZERO;
        BigDecimal totalCommission = BigDecimal.ZERO;

        for (PersonOrderDto person : request.getPeople()) {
            BigDecimal personalTotal = sumItems(person.getPersonalItems());
            BigDecimal baseTotal = personalTotal.add(sharedPerPerson);

            BigDecimal commission = baseTotal
                .multiply(request.getCommissionPercent())
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);

            BigDecimal totalToPay = baseTotal.add(commission);

            shares.add(new PersonShareDto(
                person.getName(),
                personalTotal.setScale(2, RoundingMode.HALF_UP),
                sharedPerPerson,
                commission,
                totalToPay
            ));

            totalWithoutCommission = totalWithoutCommission.add(baseTotal);
            totalCommission = totalCommission.add(commission);
        }

        BigDecimal totalWithCommission = totalWithoutCommission.add(totalCommission);

        return new BillSplitResponse(
            shares,
            totalWithoutCommission.setScale(2, RoundingMode.HALF_UP),
            totalCommission.setScale(2, RoundingMode.HALF_UP),
            totalWithCommission.setScale(2, RoundingMode.HALF_UP)
        );
    }

    private BigDecimal sumItems(List<ItemDto> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .map(ItemDto::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}