package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.ListOfUserLotteryTicketsResponseDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.service.UserLotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.USERID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.VALIDATION_FAILED;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserLotteryController.class)
class UserLotteryControllerTest {

    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    UserLotteryService userLotteryService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    @DisplayName("when perform on POST: /users/0000000001/lotteries/123456 should return id : 123456")
    void buyLotteryTickets_shouldBeSuccess() throws Exception {
        String dummyUserId = "0000000001";
        String dummyTicketId = "123456";

        UserLottery userLottery = new UserLottery();
        userLottery.setId(1);

        when(userLotteryService.buyLotteryTickets(dummyUserId, dummyTicketId)).thenReturn(userLottery);

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", dummyUserId, dummyTicketId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("when perform on GET: /users/0000000001/lotteries should return list of my lottery ticket")
    void listOfUserLotteryTickets_shouldBeSuccess() throws Exception {
        String dummyUserId = "0000000001";
        List<String> tickets = List.of("123456");

        ListOfUserLotteryTicketsResponseDto response = new ListOfUserLotteryTicketsResponseDto(tickets, 1, BigDecimal.valueOf(80));

        when(userLotteryService.listOfUserLotteryTickets(dummyUserId)).thenReturn(response);

        mockMvc.perform(get("/users/{userId}/lotteries", dummyUserId))
                .andExpect(jsonPath("$.tickets", is(tickets)))
                .andExpect(jsonPath("$.count", is(1)))
                .andExpect(jsonPath("$.cost", is(80)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("when perform on DELETE: /users/0000000001/lotteries/123456 should return id : 123456")
    void sellBack_shouldBeSuccess() throws Exception {
        String dummyUserId = "0000000001";
        String dummyTicketId = "123456";

        Lottery lottery = Lottery.builder().id(dummyTicketId).amount(1).build();
        when(userLotteryService.sellBack(dummyUserId, dummyTicketId)).thenReturn(lottery);

        mockMvc.perform(delete("/users/{userId}/lotteries/{ticketId}", dummyUserId, dummyTicketId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticket", is(dummyTicketId)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("when perform on POST: /users/0000000001x/lotteries/123456x when request invalid should return 400")
    void testBuyLottery_whenRequestInvalid_shouldReturn400() throws Exception {
        String dummyUserId = "0000000001x";
        String dummyTicketId = "123456x";

        Map<String, String> errors = new HashMap<>();
        errors.put("userId", USERID_VALIDATE_LENGTH_MSG);
        errors.put("ticketId", TICKET_ID_VALIDATE_LENGTH_MSG);

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", dummyUserId, dummyTicketId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errors)))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILED)))
                .andExpect(jsonPath("$.path", is("/users/0000000001x/lotteries/123456x")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when perform on DELETE: /users/0000000001x/lotteries/123456x when request invalid should return 400")
    void testSellBackLottery_whenRequestInvalid_shouldReturn400() throws Exception {
        String dummyUserId = "0000000001x";
        String dummyTicketId = "123456x";

        Map<String, String> errors = new HashMap<>();
        errors.put("userId", USERID_VALIDATE_LENGTH_MSG);
        errors.put("ticketId", TICKET_ID_VALIDATE_LENGTH_MSG);

        mockMvc.perform(delete("/users/{userId}/lotteries/{ticketId}", dummyUserId, dummyTicketId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errors)))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILED)))
                .andExpect(jsonPath("$.path", is("/users/0000000001x/lotteries/123456x")))
                .andExpect(status().isBadRequest());
    }
}