package com.kbtg.bootcamp.posttest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.kbtg.bootcamp.posttest.utils.Constants.AMOUNT_IS_REQUIRED;
import static com.kbtg.bootcamp.posttest.utils.Constants.INVALID_AMOUNT_VALUE;
import static com.kbtg.bootcamp.posttest.utils.Constants.INVALID_PRICE_VALUE;
import static com.kbtg.bootcamp.posttest.utils.Constants.INVALID_TICKET_ID;
import static com.kbtg.bootcamp.posttest.utils.Constants.PRICE_IS_REQUIRED;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKET_IS_REQUIRED;

@Data
@AllArgsConstructor
public class CreateLotteryRequestDto {

    @NotNull(message = TICKET_IS_REQUIRED)
    @Size(min = 6, max = 6, message = TICKET_ID_VALIDATE_LENGTH_MSG)
    @Pattern(regexp = "[0-9]+", message = INVALID_TICKET_ID)
    String ticket;

    @NotNull(message = PRICE_IS_REQUIRED)
    @Pattern(regexp = "[0-9]+", message = INVALID_PRICE_VALUE)
    String price;

    @NotNull(message = AMOUNT_IS_REQUIRED)
    @Pattern(regexp = "[0-9]+", message = INVALID_AMOUNT_VALUE)
    String amount;
}

