package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.ListOfUserLotteryTicketsResponseDto;
import com.kbtg.bootcamp.posttest.exception.PersistenceFailureException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.repository.UserLotteryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.ERROR_OCCURRED_BUY_LOTTERY;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.ERROR_TICKET_NOT_FOUND;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.NO_RESOURCE_FOUND_TO_SELL_BACK;

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
        Lottery lottery = lotteryRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_TICKET_NOT_FOUND));

        int buyUnit = 1;
        lottery.buyLottery(buyUnit);

        try {
            UserLottery userLottery = userLotteryRepository.save(UserLottery.builder()
                    .lottery(lottery)
                    .userId(userId)
                    .price(lottery.getPrice())
                    .amount(buyUnit)
                    .build());

            lotteryRepository.save(lottery);

            log.info("Successfully buy lottery tickets");
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
            tickets.add(item.getLottery().getId());
        }

        return new ListOfUserLotteryTicketsResponseDto(tickets, count, cost);
    }

    @Transactional
    public Lottery sellBack(String userId, String ticketId) {
        List<UserLottery> userLotteries = userLotteryRepository.findByUserIdAndTicketId(userId, ticketId);

        if(CollectionUtils.isEmpty(userLotteries)){
            throw new ResourceNotFoundException(NO_RESOURCE_FOUND_TO_SELL_BACK);
        }

        UserLottery userLottery = userLotteries.get(0);
        Lottery lottery = userLottery.getLottery();

        lottery.sellBackLottery(userLotteries.size());
        userLottery.setLottery(lottery);

        userLotteryRepository.deleteAll(userLotteries);
        log.info("Successfully sell back a lottery tickets");
        return lottery;
    }
}