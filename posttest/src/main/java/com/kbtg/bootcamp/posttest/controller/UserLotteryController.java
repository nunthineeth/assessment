package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.BuyLotteryResponseDto;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.service.UserLotteryService;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kbtg.bootcamp.posttest.utils.Constants.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.Constants.USERID_VALIDATE_LENGTH_MSG;

@RestController
@RequestMapping("")
@Validated
public class UserLotteryController {

    private final UserLotteryService userLotteryService;

    public UserLotteryController(UserLotteryService userLotteryService) {
        this.userLotteryService = userLotteryService;
    }

    @PostMapping("/users/{userId}/lotteries/{ticketId}")
    public ResponseEntity buyLotteryTickets(@PathVariable @Size(min = 10, max = 10, message = USERID_VALIDATE_LENGTH_MSG) String userId,
                                            @PathVariable @Size(min = 6, max = 6, message = TICKET_ID_VALIDATE_LENGTH_MSG) String ticketId) {
        UserLottery response = userLotteryService.buyLotteryTickets(userId, ticketId);
        return new ResponseEntity(new BuyLotteryResponseDto(response.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/lotteries")
    public ResponseEntity listOfUserLotteryTickets(@PathVariable @Size(min = 10, max = 10, message = USERID_VALIDATE_LENGTH_MSG) String userId) {
        return ResponseEntity.ok(userLotteryService.listOfUserLotteryTickets(userId));
    }

    @DeleteMapping("/users/{userId}/lotteries/{ticketId}")
    public ResponseEntity sellBack(@PathVariable @Size(min = 10, max = 10, message = USERID_VALIDATE_LENGTH_MSG) String userId,
                                   @PathVariable @Size(min = 6, max = 6, message = TICKET_ID_VALIDATE_LENGTH_MSG) String ticketId) {
        return ResponseEntity.ok(userLotteryService.sellBack(userId, ticketId));
    }
}
