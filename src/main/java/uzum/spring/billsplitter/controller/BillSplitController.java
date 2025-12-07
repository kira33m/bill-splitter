package uzum.spring.billsplitter.controller;

import uzum.spring.billsplitter.dto.request.BillSplitRequest;
import uzum.spring.billsplitter.dto.response.BillSplitResponse;
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
    public BillSplitResponse splitBill(@Valid @RequestBody BillSplitRequest request) {
        return billSplitService.splitBill(request);
    }
}