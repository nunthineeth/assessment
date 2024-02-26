package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.TicketsResponseDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LotteryController.class)
class LotteryControllerTest {

    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    LotteryService lotteryService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    @DisplayName("when perform on GET: /lotteries should return tickets:['111111', '222222']")
    void testGetLotteries_shouldBeSuccess() throws Exception {
        TicketsResponseDto response = new TicketsResponseDto(List.of("111111", "222222"));

        when(lotteryService.getLotteries()).thenReturn(response);

        mockMvc.perform(get("/lotteries"))
                .andExpect(jsonPath("$.tickets[0]", is("111111")))
                .andExpect(jsonPath("$.tickets[1]", is("222222")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("when perform on GET: /lotteries/:id should return lottery information")
    void testGetLotteryById_shouldBeSuccess() throws Exception {

        Lottery lottery = Lottery.builder()
                .id("111111")
                .price(BigDecimal.valueOf(80))
                .amount(10)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(lotteryService.getLotteryById(any())).thenReturn(lottery);

        mockMvc.perform(get("/lotteries/111111"))
                .andExpect(jsonPath("$.id", is("111111")))
                .andExpect(jsonPath("$.price", is(80)))
                .andExpect(jsonPath("$.amount", is(10)))
                .andExpect(status().isOk());
    }
}