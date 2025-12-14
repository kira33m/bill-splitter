package uzum.spring.billsplitter.controller;

import uzum.spring.billsplitter.dto.request.BillSplitRequestDto;
import uzum.spring.billsplitter.dto.response.BillSplitResponseDto;
import uzum.spring.billsplitter.service.BillSplitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillSplitController {

    private final BillSplitService billSplitService;

    @PostMapping("/split")
    public BillSplitResponseDto splitBill(@Valid @RequestBody BillSplitRequestDto request) {
        return billSplitService.splitBill(request);
    }
}