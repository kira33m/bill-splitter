package uzum.spring.billsplitter.controller;

import uzum.spring.billsplitter.dto.response.BillSplitResponseDto;
import uzum.spring.billsplitter.dto.response.PersonShareDto;
import uzum.spring.billsplitter.service.BillSplitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BillSplitController.class)
@AutoConfigureMockMvc(addFilters = false)
class BillSplitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillSplitService billSplitService;

    @Test
    void splitBill_returnsCalculatedResponse() throws Exception {
        BillSplitResponseDto mockResponse = BillSplitResponseDto.builder()
            .shares(List.of(
                PersonShareDto.builder()
                    .name("Alice")
                    .personalTotal(new BigDecimal("300.00"))
                    .sharedTotal(new BigDecimal("200.00"))
                    .commission(new BigDecimal("50.00"))
                    .totalToPay(new BigDecimal("550.00"))
                    .build()
            ))
            .totalWithoutCommission(new BigDecimal("500.00"))
            .totalCommission(new BigDecimal("50.00"))
            .totalWithCommission(new BigDecimal("550.00"))
            .build();

        given(billSplitService.splitBill(any())).willReturn(mockResponse);

        String requestJson = """
            {
              "people": [
                {
                  "name": "Alice",
                  "personalItems": [
                    { "name": "Burger", "price": 300.00 }
                  ]
                }
              ],
              "sharedItems": [
                { "name": "BigPizza", "price": 200.00 }
              ],
              "commissionPercent": 10.0
            }
            """;

        mockMvc.perform(post("/api/bills/split")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shares[0].name").value("Alice"))
            .andExpect(jsonPath("$.shares[0].totalToPay").value(550.00))
            .andExpect(jsonPath("$.totalWithCommission").value(550.00));
    }

    @Test
    void splitBill_returnsBadRequest_whenValidationFails() throws Exception {
        String invalidJson = """
            {
              "people": [],
              "sharedItems": [],
              "commissionPercent": -5.0
            }
            """;

        mockMvc.perform(post("/api/bills/split")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    void splitBill_returnsBadRequest_whenPersonNameIsBlank() throws Exception {
        String invalidJson = """
            {
              "people": [
                {
                  "name": "",
                  "personalItems": []
                }
              ],
              "sharedItems": [],
              "commissionPercent": 10.0
            }
            """;

        mockMvc.perform(post("/api/bills/split")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors[?(@.field == 'people[0].name')]").exists());
    }

    @Test
    void splitBill_returnsBadRequest_whenItemPriceIsNegative() throws Exception {
        String invalidJson = """
            {
              "people": [
                {
                  "name": "Alice",
                  "personalItems": [
                    { "name": "Burger", "price": -100.00 }
                  ]
                }
              ],
              "sharedItems": [],
              "commissionPercent": 10.0
            }
            """;

        mockMvc.perform(post("/api/bills/split")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors[?(@.field == 'people[0].personalItems[0].price')]").exists());
    }

    @Test
    void splitBill_returnsBadRequest_whenCommissionPercentExceedsMax() throws Exception {
        String invalidJson = """
            {
              "people": [
                {
                  "name": "Alice",
                  "personalItems": []
                }
              ],
              "sharedItems": [],
              "commissionPercent": 150.0
            }
            """;

        mockMvc.perform(post("/api/bills/split")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors[?(@.field == 'commissionPercent')]").exists());
    }
}