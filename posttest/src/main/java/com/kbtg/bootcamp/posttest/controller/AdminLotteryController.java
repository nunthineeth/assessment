package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/lotteries")
public class AdminLotteryController {

    private final LotteryService lotteryService;

    public AdminLotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody CreateLotteryRequestDto request) {
        Lottery lottery = lotteryService.create(request);
        return new ResponseEntity(new TicketResponseDto(lottery.getTicketId()), HttpStatus.CREATED);
    }
}
