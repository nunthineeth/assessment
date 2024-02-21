package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import com.kbtg.bootcamp.posttest.dto.TicketsResponseDto;
import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_ALREADY_EXIST;
import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {

    @InjectMocks
    LotteryService lotteryService;

    @Mock
    LotteryRepository lotteryRepository;

    final String TICKET_ID = "111111";
    final BigDecimal PRICE = BigDecimal.valueOf(80);
    final int AMOUNT = 1;

    @Test
    @DisplayName("given create lottery tickets should success")
    void testCreateLotteryTicket_shouldSuccess() {
        Lottery lottery = mockLottery(TICKET_ID, PRICE, AMOUNT);

        when(lotteryRepository.findByTicketId(any())).thenReturn(Optional.empty());
        when(lotteryRepository.save(any(Lottery.class))).thenReturn(lottery);

        Lottery actual = lotteryService.create(mockCreateLotteryRequest(TICKET_ID, PRICE.toString(), String.valueOf(AMOUNT)));

        verify(lotteryRepository, times(1)).findByTicketId(any());
        verify(lotteryRepository, times(1)).save(any(Lottery.class));

        assertEquals(TICKET_ID, actual.getTicketId());
        assertEquals(PRICE, actual.getPrice());
        assertEquals(AMOUNT, actual.getAmount());
        assertEquals(false, actual.isDeleted());
    }

    @Test
    @DisplayName("given create lottery tickets already created should return error")
    void testCreateLotteryTicket_whenAlreadyCreated_shouldReturnError() {
        Optional<Lottery> lotteryOptional = Optional.of(mockLottery(TICKET_ID, PRICE, AMOUNT));

        when(lotteryRepository.findByTicketId(any())).thenReturn(lotteryOptional);

        BusinessValidationException actualException = assertThrows(BusinessValidationException.class, () -> {
            lotteryService.create(mockCreateLotteryRequest(TICKET_ID, PRICE.toString(), String.valueOf(AMOUNT)));
        });

        assertEquals(ERROR_TICKET_ALREADY_EXIST, actualException.getMessage());
        verify(lotteryRepository, times(1)).findByTicketId(TICKET_ID);
        verify(lotteryRepository, never()).save(any(Lottery.class));
    }

    @Test
    @DisplayName("given find lottery by ticketId should success")
    void testFindById_shouldSuccess() {
        Lottery lottery = mockLottery(TICKET_ID, PRICE, AMOUNT);
        when(lotteryRepository.findById(any())).thenReturn(Optional.of(lottery));

        Lottery actual = lotteryService.getLotteryById(TICKET_ID);

        assertNotNull(actual);
        assertEquals(TICKET_ID, actual.getTicketId());
        verify(lotteryRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("given find lottery by ticketId when not found")
    void testFindById_whenNotFoundLotteryTicket_shouldReturnError() {
        when(lotteryRepository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException actualException = assertThrows(ResourceNotFoundException.class, () -> {
            lotteryService.getLotteryById(TICKET_ID);
        });

        assertEquals(ERROR_TICKET_NOT_FOUND, actualException.getMessage());
        verify(lotteryRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("given get lotteries should success")
    void testGetLotteries_shouldBeSuccess() {
        List<String> expected = List.of(TICKET_ID);
        when(lotteryRepository.findAllTickets()).thenReturn(expected);

        TicketsResponseDto actual = lotteryService.getLotteries();

        assertNotNull(actual);
        assertThat(actual.tickets(), is(expected));
        verify(lotteryRepository, times(1)).findAllTickets();
    }

    private static Lottery mockLottery(String ticketId, BigDecimal price, int amount) {
        Lottery lottery = Lottery.builder()
                .ticketId(ticketId)
                .price(price)
                .amount(amount)
                .build();
        return lottery;
    }

    private CreateLotteryRequestDto mockCreateLotteryRequest(String ticketId, String price, String amount) {
        return new CreateLotteryRequestDto(ticketId, price, amount);
    }
}
