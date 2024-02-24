package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.security.SecurityConfig;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static com.kbtg.bootcamp.posttest.utils.Constants.AMOUNT_IS_REQUIRED;
import static com.kbtg.bootcamp.posttest.utils.Constants.INVALID_PRICE_VALUE;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.Constants.VALIDATION_FAILED;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdminLotteryController.class)
class AdminLotteryControllerTest {

    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    LotteryService lotteryService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("when perform on POST: /admin/lotteries when no authentication should return 401")
    void testCreateLottery_whenNoAuthentication_shouldReturn401() throws Exception {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto("111111", "80", "10");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("when perform on POST: /admin/lotteries when user is not admin should return 403")
    void testCreateLottery_whenUserIsNotAdmin_shouldReturn403() throws Exception {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto("111111", "80", "10");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("when perform on POST: /admin/lotteries should return ticket : 111111")
    void testCreateLottery_shouldBeSuccess() throws Exception {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto("111111", "80", "10");

        Lottery lottery = new Lottery();
        lottery.setId("111111");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        when(lotteryService.create(request)).thenReturn(lottery);

        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.ticket", is("111111")))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("when perform on POST: /admin/lotteries when request invalid should return 400")
    void testCreateLottery_whenRequestInvalid_shouldReturn400() throws Exception {
        String json = "{\"ticket\":\"1234567\",\"price\":-80,\"amount\":null}";

        Map<String, String> errors = new HashMap<>();
        errors.put("ticket", TICKET_ID_VALIDATE_LENGTH_MSG);
        errors.put("price", INVALID_PRICE_VALUE);
        errors.put("amount", AMOUNT_IS_REQUIRED);

        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errors)))
                .andExpect(jsonPath("$.message", is(VALIDATION_FAILED)))
                .andExpect(jsonPath("$.path", is("/admin/lotteries")))
                .andExpect(status().isBadRequest());
    }
}