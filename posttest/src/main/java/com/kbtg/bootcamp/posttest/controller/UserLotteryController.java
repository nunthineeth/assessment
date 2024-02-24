package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.BuyLotteryResponseDto;
import com.kbtg.bootcamp.posttest.dto.ListOfUserLotteryTicketsResponseDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.service.UserLotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_OCCURRED_BUY_LOTTERY;
import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_NOT_FOUND;
import static com.kbtg.bootcamp.posttest.utils.Constants.NO_RESOURCE_FOUND_TO_SELL_BACK;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKETS_HAVE_BEEN_SOLD_OUT;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.Constants.USERID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.Constants.VALIDATION_FAILED;

@RestController
@RequestMapping("")
@Validated
public class UserLotteryController {

    private final UserLotteryService userLotteryService;

    public UserLotteryController(UserLotteryService userLotteryService) {
        this.userLotteryService = userLotteryService;
    }

    @Operation(summary = "Buy lottery tickets")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Buy Success",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BuyLotteryResponseDto.class)))
                    }),
            @ApiResponse(responseCode = "400", description = VALIDATION_FAILED),
            @ApiResponse(responseCode = "400", description = TICKETS_HAVE_BEEN_SOLD_OUT),
            @ApiResponse(responseCode = "404", description = ERROR_TICKET_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ERROR_OCCURRED_BUY_LOTTERY)
    })
    @PostMapping("/users/{userId}/lotteries/{ticketId}")
    public ResponseEntity buyLotteryTickets(@PathVariable @Size(min = 10, max = 10, message = USERID_VALIDATE_LENGTH_MSG)
                                            @Parameter(name = "userId", description = "User id", example = "1111111111") String userId,
                                            @PathVariable @Size(min = 6, max = 6, message = TICKET_ID_VALIDATE_LENGTH_MSG)
                                            @Parameter(name = "ticketId", description = "Ticket Id", example = "123456") String ticketId) {
        UserLottery response = userLotteryService.buyLotteryTickets(userId, ticketId);
        return new ResponseEntity(new BuyLotteryResponseDto(response.getId()), HttpStatus.CREATED);
    }

    @Operation(summary = "List of user lottery tickets")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ListOfUserLotteryTicketsResponseDto.class)))
                    })
    })
    @GetMapping("/users/{userId}/lotteries")
    public ResponseEntity listOfUserLotteryTickets(@PathVariable @Parameter(name = "userId", description = "User id", example = "1111111111") String userId) {
        return ResponseEntity.ok(userLotteryService.listOfUserLotteryTickets(userId));
    }

    @Operation(summary = "Sell back lottery tickets")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TicketResponseDto.class)))
                    }),
            @ApiResponse(responseCode = "400", description = VALIDATION_FAILED),
            @ApiResponse(responseCode = "404", description = NO_RESOURCE_FOUND_TO_SELL_BACK),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/users/{userId}/lotteries/{ticketId}")
    public ResponseEntity sellBack(@PathVariable @Size(min = 10, max = 10, message = USERID_VALIDATE_LENGTH_MSG)
                                   @Parameter(name = "userId", description = "User id", example = "1111111111") String userId,
                                   @PathVariable @Size(min = 6, max = 6, message = TICKET_ID_VALIDATE_LENGTH_MSG)
                                   @Parameter(name = "ticketId", description = "Ticket Id", example = "123456") String ticketId) {
        Lottery lottery = userLotteryService.sellBack(userId, ticketId);
        return ResponseEntity.ok(new TicketResponseDto(lottery.getId()));
    }
}
