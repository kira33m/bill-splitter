package uzum.spring.billsplitter.service.impl;

import uzum.spring.billsplitter.dto.request.BillSplitRequestDto;
import uzum.spring.billsplitter.dto.request.ItemDto;
import uzum.spring.billsplitter.dto.request.PersonOrderDto;
import uzum.spring.billsplitter.dto.response.BillSplitResponseDto;
import uzum.spring.billsplitter.dto.response.PersonShareDto;
import uzum.spring.billsplitter.exception.EmptyPeopleListException;
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
    public BillSplitResponseDto splitBill(BillSplitRequestDto request) {
        if (request.people() == null || request.people().isEmpty()) {
            throw new EmptyPeopleListException();
        }

        int peopleCount = request.people().size();

        BigDecimal sharedTotal = sumItems(request.sharedItems());
        BigDecimal sharedPerPerson = peopleCount > 0
            ? sharedTotal.divide(BigDecimal.valueOf(peopleCount), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        List<PersonShareDto> shares = new ArrayList<>();
        BigDecimal totalWithoutCommission = BigDecimal.ZERO;
        BigDecimal totalCommission = BigDecimal.ZERO;

        for (PersonOrderDto person : request.people()) {
            BigDecimal personalTotal = sumItems(person.personalItems());
            BigDecimal baseTotal = personalTotal.add(sharedPerPerson);

            BigDecimal commission = baseTotal
                .multiply(request.commissionPercent())
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);

            BigDecimal totalToPay = baseTotal.add(commission);

            shares.add(PersonShareDto.builder()
                .name(person.name())
                .personalTotal(personalTotal.setScale(2, RoundingMode.HALF_UP))
                .sharedTotal(sharedPerPerson)
                .commission(commission)
                .totalToPay(totalToPay)
                .build());

            totalWithoutCommission = totalWithoutCommission.add(baseTotal);
            totalCommission = totalCommission.add(commission);
        }

        BigDecimal totalWithCommission = totalWithoutCommission.add(totalCommission);

        return BillSplitResponseDto.builder()
            .shares(shares)
            .totalWithoutCommission(totalWithoutCommission.setScale(2, RoundingMode.HALF_UP))
            .totalCommission(totalCommission.setScale(2, RoundingMode.HALF_UP))
            .totalWithCommission(totalWithCommission.setScale(2, RoundingMode.HALF_UP))
            .build();
    }

    private BigDecimal sumItems(List<ItemDto> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .map(ItemDto::price)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}