package com.kbtg.bootcamp.posttest.dto;

import java.math.BigDecimal;
import java.util.List;

public record ListOfUserLotteryTicketsResponseDto(List<String> tickets, int count, BigDecimal cost) {
}
