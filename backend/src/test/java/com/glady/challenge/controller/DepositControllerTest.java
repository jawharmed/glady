package com.glady.challenge.controller;

import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.service.deposit.DepositService;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.glady.challenge.helpers.Utils.asJsonString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepositController.class)
class DepositControllerTest {

    @MockBean
    private DepositService depositService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testMakeDeposit_WithValidDeposit_ShouldReturnDepositDTO() throws Exception {
        DepositDTO depositDTO = DtoObjectHelper.getDepositGiftDTO();
        when(depositService.makeDeposit(depositDTO)).thenReturn(depositDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(depositDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(depositDTO.getId()))
                .andExpect(jsonPath("$.amount").value(depositDTO.getAmount()))
                .andExpect(jsonPath("$.depositType").value(depositDTO.getDepositType()));

        verify(depositService).makeDeposit(depositDTO);
    }


}
