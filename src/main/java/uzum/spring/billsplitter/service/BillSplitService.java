package uzum.spring.billsplitter.service;

import uzum.spring.billsplitter.dto.request.BillSplitRequest;
import uzum.spring.billsplitter.dto.response.BillSplitResponse;

public interface BillSplitService {

    BillSplitResponse splitBill(BillSplitRequest request);
}