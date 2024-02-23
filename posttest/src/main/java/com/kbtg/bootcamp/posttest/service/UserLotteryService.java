package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.ListOfUserLotteryTicketsResponseDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import com.kbtg.bootcamp.posttest.exception.PersistenceFailureException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.repository.UserLotteryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_OCCURRED_BUY_LOTTERY;
import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_NOT_FOUND;
import static com.kbtg.bootcamp.posttest.utils.Constants.NO_RESOURCE_FOUND_TO_SELL_BACK;
import static com.kbtg.bootcamp.posttest.utils.Constants.TICKETS_HAVE_BEEN_SOLD;

@Slf4j
@Service
public class UserLotteryService {

    private final LotteryRepository lotteryRepository;
    private final UserLotteryRepository userLotteryRepository;

    public UserLotteryService(LotteryRepository lotteryRepository,
                              UserLotteryRepository userLotteryRepository) {
        this.lotteryRepository = lotteryRepository;
        this.userLotteryRepository = userLotteryRepository;
    }

    @Transactional
    public UserLottery buyLotteryTickets(String userId, String ticketId) {
        Lottery lottery = lotteryRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_TICKET_NOT_FOUND));

        if(lottery.isDeleted()){
            throw new BusinessValidationException(TICKETS_HAVE_BEEN_SOLD);
        }

        try {
            UserLottery userLottery = userLotteryRepository.save(UserLottery.builder()
                    .lottery(lottery)
                    .userId(userId)
                    .price(lottery.getPrice())
                    .amount(lottery.getAmount())
                    .build());

            lottery.setDeleted(true);
            lotteryRepository.save(lottery);

            log.info("Successfully bought lottery tickets");
            return userLottery;
        } catch (Exception e) {
            log.error(String.format("%s : %s", ERROR_OCCURRED_BUY_LOTTERY, e.getMessage()));
            throw new PersistenceFailureException(ERROR_OCCURRED_BUY_LOTTERY);
        }
    }

    public ListOfUserLotteryTicketsResponseDto listOfUserLotteryTickets(String userId) {
        List<UserLottery> userLotteryList = userLotteryRepository.findByUserId(userId);

        BigDecimal cost = BigDecimal.ZERO;
        int count = 0;
        List<String> tickets = new ArrayList<>();
        for (UserLottery item : userLotteryList) {
            cost = cost.add(item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())));
            count = count + item.getAmount();
            tickets.add(item.getLottery().getTicketId());
        }

        return new ListOfUserLotteryTicketsResponseDto(tickets, count, cost);
    }

    @Transactional
    public TicketResponseDto sellBack(String userId, String ticketId) {
        UserLottery userLottery = userLotteryRepository.findByUserIdAndTicketId(userId, ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RESOURCE_FOUND_TO_SELL_BACK));

        Lottery lottery = userLottery.getLottery();
        lottery.setDeleted(false);
        userLottery.setLottery(lottery);

        userLotteryRepository.delete(userLottery);
        log.info("Successfully sell back a lottery tickets");
        return new TicketResponseDto(lottery.getTicketId());
    }
}