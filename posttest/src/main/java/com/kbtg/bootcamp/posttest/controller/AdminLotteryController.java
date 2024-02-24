package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_ALREADY_EXIST;
import static com.kbtg.bootcamp.posttest.utils.Constants.VALIDATION_FAILED;

@RestController
@RequestMapping("/admin/lotteries")
public class AdminLotteryController {

    private final LotteryService lotteryService;

    public AdminLotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @Operation(summary = "Create lottery tickets")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Create Success",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TicketResponseDto.class)))
                    }),
            @ApiResponse(responseCode = "400", description = VALIDATION_FAILED),
            @ApiResponse(responseCode = "400", description = ERROR_TICKET_ALREADY_EXIST),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody CreateLotteryRequestDto request) {
        Lottery lottery = lotteryService.create(request);
        return new ResponseEntity(new TicketResponseDto(lottery.getId()), HttpStatus.CREATED);
    }
}
