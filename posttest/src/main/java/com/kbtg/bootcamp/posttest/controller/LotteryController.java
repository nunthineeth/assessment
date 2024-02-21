package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.service.LotteryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lotteries")
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping("")
    public ResponseEntity getLotteries() {
        return ResponseEntity.ok(lotteryService.getLotteries());
    }

    @GetMapping("/{id}")
    public ResponseEntity getLotteryById(@PathVariable String id) {
        return ResponseEntity.ok(lotteryService.getLotteryById(id));
    }
}
