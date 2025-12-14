package uzum.spring.billsplitter.service;

import uzum.spring.billsplitter.dto.request.BillSplitRequestDto;
import uzum.spring.billsplitter.dto.response.BillSplitResponseDto;

public interface BillSplitService {

    BillSplitResponseDto splitBill(BillSplitRequestDto request);
}