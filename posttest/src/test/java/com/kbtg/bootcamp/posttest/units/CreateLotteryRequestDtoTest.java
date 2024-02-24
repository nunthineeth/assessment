package com.kbtg.bootcamp.posttest.units;

import com.kbtg.bootcamp.posttest.dto.CreateLotteryRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.AMOUNT_IS_REQUIRED;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.INVALID_AMOUNT_VALUE;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.INVALID_PRICE_VALUE;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.INVALID_TICKET_ID;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.PRICE_IS_REQUIRED;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.TICKET_ID_VALIDATE_LENGTH_MSG;
import static com.kbtg.bootcamp.posttest.utils.ErrorMessage.TICKET_IS_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateLotteryRequestDtoTest {

    private static Validator validator;

    private final String TICKET_VALID = "123456";
    private final String PRICE_VALID = "80";
    private final String AMOUNT_VALID = "1";

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void ticketLength_shouldBe6Characters() {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, PRICE_VALID, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void ticket_givenLengthIsMore6Char_shouldBeInvalid() {
        String dummyTicket = "1234567";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(dummyTicket, PRICE_VALID, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(TICKET_ID_VALIDATE_LENGTH_MSG);
    }

    @Test
    void ticket_givenNull_shouldBeInvalid() {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(null, PRICE_VALID, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(TICKET_IS_REQUIRED);
    }

    @Test
    void ticket_givenNotNumber_shouldBeInvalid() {
        String dummyTicket = "12345a";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(dummyTicket, PRICE_VALID, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(INVALID_TICKET_ID);
    }

    @Test
    void price_shouldBePositive() {
        String dummyPrice = "80";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, dummyPrice, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void price_givenNegative_shouldBeInvalid() {
        String dummyPrice = "-100";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, dummyPrice, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(INVALID_PRICE_VALUE);
    }

    @Test
    void price_givenNotNumber_shouldBeInvalid() {
        String dummyPrice = "10a";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, dummyPrice, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(INVALID_PRICE_VALUE);
    }

    @Test
    void price_givenNull_shouldBeInvalid() {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, null, AMOUNT_VALID);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(PRICE_IS_REQUIRED);
    }

    @Test
    void amount_shouldBePositive() {
        String dummyAmount = "1";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, PRICE_VALID, dummyAmount);

        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void amount_givenNegative_shouldBeInvalid() {
        String dummyAmount = "-1";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, PRICE_VALID, dummyAmount);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(INVALID_AMOUNT_VALUE);
    }

    @Test
    void amount_givenNotNumber_shouldBeInvalid() {
        String dummyAmount = "10a";
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, PRICE_VALID, dummyAmount);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(INVALID_AMOUNT_VALUE);
    }

    @Test
    void amount_givenNull_shouldBeInvalid() {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(TICKET_VALID, PRICE_VALID, null);

        var violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        Iterator<ConstraintViolation<CreateLotteryRequestDto>> iterator = violations.iterator();
        assertThat(iterator.next().getMessage()).isEqualTo(AMOUNT_IS_REQUIRED);
    }

    @Test
    void ticketAndPriceAndAmountInvalid() {
        CreateLotteryRequestDto request = new CreateLotteryRequestDto(null, "-80","abc");

        var violations = validator.validate(request);

        assertThat(violations).hasSize(3);
    }
}
