package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketsResponseDto;
import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_ALREADY_EXIST;
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
        lotteryRepository.findByTicketId(request.getTicket())
                .ifPresent(e -> {
                    throw new BusinessValidationException(ERROR_TICKET_ALREADY_EXIST);
                });

        Lottery lottery = lotteryRepository.save(Lottery.builder()
                .ticketId(request.getTicket())
                .price(new BigDecimal(request.getPrice()))
                .amount(Integer.parseInt(request.getAmount()))
                .isDeleted(false)
                .build());

        log.info("Successfully create lottery tickets");
        return lottery;
    }

    public Lottery getLotteryById(String id) {
        return lotteryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ERROR_TICKET_NOT_FOUND));
    }

    public TicketsResponseDto getLotteries() {
        List<String> tickets = lotteryRepository.findAllTickets();
        return new TicketsResponseDto(tickets);
    }
}
