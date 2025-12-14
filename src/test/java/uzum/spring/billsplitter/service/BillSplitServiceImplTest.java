package uzum.spring.billsplitter.service;

import uzum.spring.billsplitter.dto.request.*;
import uzum.spring.billsplitter.dto.response.*;
import uzum.spring.billsplitter.service.impl.BillSplitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BillSplitServiceImplTest {

    @InjectMocks
    private BillSplitServiceImpl service;

    @Test
    void splitBill_calculatesCorrectly() {
        BillSplitRequestDto request = new BillSplitRequestDto(
            List.of(
                new PersonOrderDto("Alice", List.of(
                    new ItemDto("Burger", BigDecimal.valueOf(300))
                )),
                new PersonOrderDto("Bob", List.of(
                    new ItemDto("Salad", BigDecimal.valueOf(200))
                )),
                new PersonOrderDto("Charlie", List.of(
                    new ItemDto("Pasta", BigDecimal.valueOf(400))
                )),
                new PersonOrderDto("Diana", List.of())
            ),
            List.of(new ItemDto("BigPizza", BigDecimal.valueOf(800))),
            BigDecimal.valueOf(10)
        );

        BillSplitResponseDto response = service.splitBill(request);

        assertEquals(4, response.shares().size());

        PersonShareDto alice = response.shares().stream()
            .filter(s -> s.name().equals("Alice"))
            .findFirst()
            .orElseThrow();

        assertEquals(new BigDecimal("300.00"), alice.personalTotal());
        assertEquals(new BigDecimal("200.00"), alice.sharedTotal());
        assertEquals(new BigDecimal("50.00"), alice.commission());
        assertEquals(new BigDecimal("550.00"), alice.totalToPay());
    }
}