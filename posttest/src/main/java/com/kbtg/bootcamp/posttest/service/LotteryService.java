package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketsResponseDto;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_NOT_FOUND;

@Slf4j
@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;

    public LotteryService(LotteryRepository lotteryRepository) {
        this.lotteryRepository = lotteryRepository;
    }

    @Transactional
    public Lottery create(CreateLotteryRequestDto request) {
        Optional<Lottery> lotteryOptional = lotteryRepository.findById(request.getTicket());

        Lottery lottery;

        if (lotteryOptional.isPresent()) {
            lottery = lotteryOptional.get();
            lottery.setAmount(lottery.getAmount() + Integer.parseInt(request.getAmount()));
        } else {
            lottery = Lottery.builder()
                    .id(request.getTicket())
                    .price(new BigDecimal(request.getPrice()))
                    .amount(Integer.parseInt(request.getAmount()))
                    .build();
        }

        lotteryRepository.save(lottery);
        log.info("Successfully create lottery tickets");
        return lottery;
    }

    public Lottery getLotteryById(String id) {
        return lotteryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_TICKET_NOT_FOUND));
    }

    public TicketsResponseDto getLotteries() {
        List<Lottery> lotteries = lotteryRepository.findRemainingTickets();

        List<String> tickets = new ArrayList<>();
        lotteries.stream().forEach(o -> {
            int amount = o.getAmount();
            for (int i = 1; i <= amount; i++) {
                tickets.add(o.getId());
            }
        });

        return new TicketsResponseDto(tickets);
    }
}
