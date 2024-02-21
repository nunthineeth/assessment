package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.dto.ListOfUserLotteryTicketsResponseDto;
import com.kbtg.bootcamp.posttest.dto.TicketResponseDto;
import com.kbtg.bootcamp.posttest.exception.PersistenceFailureException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.model.Lottery;
import com.kbtg.bootcamp.posttest.model.UserLottery;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.repository.UserLotteryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_OCCURRED_BUY_LOTTERY;
import static com.kbtg.bootcamp.posttest.utils.Constants.NO_RESOURCE_FOUND_TO_SELL_BACK;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserLotteryServiceTest {

    @InjectMocks
    UserLotteryService userLotteryService;
    @Mock
    LotteryRepository lotteryRepository;
    @Mock
    UserLotteryRepository userLotteryRepository;

    final String TICKET_ID = "123456";
    final String USER_ID = "00000000001";
    final BigDecimal PRICE = BigDecimal.valueOf(80);
    final int AMOUNT = 1;
    final LocalDateTime now = LocalDateTime.now();

    @Test
    @DisplayName("given buy lottery tickets should success")
    void testBuyLotteryTickets_shouldSuccess() {
        Lottery lottery = mockLottery(TICKET_ID, PRICE, AMOUNT);
        Optional<Lottery> lotteryOptional = Optional.of(lottery);

        UserLottery userLottery = mockUserLottery(lottery);

        when(lotteryRepository.findByTicketId(any())).thenReturn(lotteryOptional);
        when(userLotteryRepository.save(any(UserLottery.class))).thenReturn(userLottery);
        when(lotteryRepository.save(any(Lottery.class))).thenReturn(lottery);

        UserLottery actual = userLotteryService.buyLotteryTickets(TICKET_ID, USER_ID);

        verify(lotteryRepository, times(1)).findByTicketId(any());
        verify(userLotteryRepository, times(1)).save(any(UserLottery.class));
        verify(lotteryRepository, times(1)).save(any(Lottery.class));

        assertEquals(1, actual.getId());
        assertEquals(USER_ID, actual.getUserId());
        assertEquals(TICKET_ID, actual.getLottery().getTicketId());
        assertEquals(true, actual.getLottery().isDeleted());
        assertEquals(now, actual.getCreatedDate());
        assertEquals(now, actual.getLastModifiedDate());
    }

    @Test
    @DisplayName("given buy lottery tickets when error occurred while saving should return error ")
    void testBuyLotteryTicket_givenErrorWhileSaving_shouldReturnError() {
        Lottery lottery = mockLottery(TICKET_ID, PRICE, AMOUNT);
        Optional<Lottery> lotteryOptional = Optional.of(lottery);

        UserLottery userLottery = mockUserLottery(lottery);

        when(lotteryRepository.findByTicketId(any())).thenReturn(lotteryOptional);
        when(userLotteryRepository.save(any(UserLottery.class))).thenReturn(userLottery);
        when(lotteryRepository.save(any(Lottery.class))).thenThrow(new PersistenceFailureException(ERROR_OCCURRED_BUY_LOTTERY));

        PersistenceFailureException actualException = assertThrows(PersistenceFailureException.class, () -> {
            userLotteryService.buyLotteryTickets(TICKET_ID, USER_ID);
        });

        assertEquals(ERROR_OCCURRED_BUY_LOTTERY, actualException.getMessage());
        verify(lotteryRepository, times(1)).findByTicketId(any());
        verify(userLotteryRepository, times(1)).save(any(UserLottery.class));
        verify(lotteryRepository, times(1)).save(any(Lottery.class));
    }

    @Test
    @DisplayName("given list of user lottery tickets should return tickets:['123456', '111111'], count: 6, cost: 680")
    void testListOfUserLotteryTickets_shouldSuccess() {
        String dummyTicket1 = TICKET_ID;
        String dummyTicket2 = "111111";
        Lottery lottery1 = mockLottery(dummyTicket1, PRICE, AMOUNT);
        UserLottery userLottery1 = mockUserLottery(lottery1);

        Lottery lottery2 = mockLottery(dummyTicket2, BigDecimal.valueOf(120), 5);
        UserLottery userLottery2 = mockUserLottery(lottery2);

        when(userLotteryRepository.findByUserId(any())).thenReturn(List.of(userLottery1, userLottery2));

        ListOfUserLotteryTicketsResponseDto actual = userLotteryService.listOfUserLotteryTickets(USER_ID);

        List<String> expectedTickets = Arrays.asList(dummyTicket1, dummyTicket2);
        assertNotNull(actual);
        assertArrayEquals(expectedTickets.toArray(), actual.tickets().toArray());
        assertEquals(6, actual.count());
        assertEquals(BigDecimal.valueOf(680), actual.cost());
        verify(userLotteryRepository, times(1)).findByUserId(any());
    }

    @Test
    @DisplayName("given not found list of user lottery tickets should return tickets:[], count: 0, cost: 0")
    void testListOfUserLotteryTickets_whenTicketNotFound() {
        when(userLotteryRepository.findByUserId(any())).thenReturn(Arrays.asList());

        ListOfUserLotteryTicketsResponseDto actual = userLotteryService.listOfUserLotteryTickets(USER_ID);

        assertNotNull(actual);
        assertEquals(0, actual.tickets().size());
        assertEquals(0, actual.count());
        assertEquals(BigDecimal.ZERO, actual.cost());
        verify(userLotteryRepository, times(1)).findByUserId(any());
    }

    @Test
    @DisplayName("given sell back lottery tickets should be userLottery was delete and lottery return stock")
    void testSellBackLotteryTickets_shouldSuccess() {
        Lottery lottery = mockLottery(TICKET_ID, PRICE, AMOUNT);
        UserLottery userLottery = mockUserLottery(lottery);
        when(userLotteryRepository.findByUserIdAndTicketId(any(), any())).thenReturn(Optional.of(userLottery));

        TicketResponseDto actual = userLotteryService.sellBack(TICKET_ID, USER_ID);

        assertNotNull(actual);
        assertEquals(TICKET_ID, actual.ticket());
        assertFalse(lottery.isDeleted());
        verify(userLotteryRepository, times(1)).delete(userLottery);
    }

    @Test
    @DisplayName("given sell back lottery tickets when userLottery not found should return error")
    void testSellBackLotteryTickets_whenUserLotteryNotFound() {
        when(userLotteryRepository.findByUserIdAndTicketId(any(), any())).thenReturn(Optional.empty());

        ResourceNotFoundException actualException = assertThrows(ResourceNotFoundException.class, () -> {
            userLotteryService.sellBack(TICKET_ID, USER_ID);
        });

        assertEquals(NO_RESOURCE_FOUND_TO_SELL_BACK, actualException.getMessage());
        verify(userLotteryRepository, times(1)).findByUserIdAndTicketId(any(), any());
        verify(userLotteryRepository, never()).delete(any(UserLottery.class));
    }

    private UserLottery mockUserLottery(Lottery lottery) {
        return UserLottery.builder()
                .id(1)
                .lottery(lottery)
                .userId(USER_ID)
                .price(lottery.getPrice())
                .amount(lottery.getAmount())
                .createdDate(now)
                .lastModifiedDate(now)
                .build();
    }


    private static Lottery mockLottery(String ticketId, BigDecimal price, int amount) {
        Lottery lottery = Lottery.builder()
                .ticketId(ticketId)
                .price(price)
                .amount(amount)
                .build();
        return lottery;
    }
}
