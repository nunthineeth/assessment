package com.kbtg.bootcamp.posttest.units;

import com.kbtg.bootcamp.posttest.dto.ErrorResponseDto;
import com.kbtg.bootcamp.posttest.exception.BusinessValidationException;
import com.kbtg.bootcamp.posttest.exception.PersistenceFailureException;
import com.kbtg.bootcamp.posttest.exception.ResourceNotFoundException;
import com.kbtg.bootcamp.posttest.filter.ErrorHandlerFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_OCCURRED_BUY_LOTTERY;
import static com.kbtg.bootcamp.posttest.utils.Constants.ERROR_TICKET_ALREADY_EXIST;
import static com.kbtg.bootcamp.posttest.utils.Constants.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerFilterTest {

    @Mock
    private MockHttpServletRequest request;
    @InjectMocks
    private ErrorHandlerFilter errorHandlerFilter;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        lenient().when(request.getRequestURI()).thenReturn("/test");
    }

    @Test
    void testHandleBusinessValidationException_shouldBusinessValidationResponse() {
        BusinessValidationException exception = new BusinessValidationException(ERROR_TICKET_ALREADY_EXIST);
        ResponseEntity<ErrorResponseDto> response = errorHandlerFilter.handleBusinessValidationException(httpServletRequest, exception);

        ErrorResponseDto errorResponse = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
        assertEquals(ERROR_TICKET_ALREADY_EXIST, errorResponse.getMessage());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleNotFoundException_ShouldReturnNotFoundResponse() {
        ResourceNotFoundException exception = new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        ResponseEntity<ErrorResponseDto> response = errorHandlerFilter.handleResourceNotFoundException(httpServletRequest, exception);

        ErrorResponseDto errorResponse = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getError());
        assertEquals(RESOURCE_NOT_FOUND, errorResponse.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHandlePersistenceFailureException_ShouldReturnPersistenceFailureResponse() {
        PersistenceFailureException exception = new PersistenceFailureException(ERROR_OCCURRED_BUY_LOTTERY);
        ResponseEntity<ErrorResponseDto> response = errorHandlerFilter.handlePersistenceFailureException(httpServletRequest, exception);

        ErrorResponseDto errorResponse = response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getError());
        assertEquals(ERROR_OCCURRED_BUY_LOTTERY, errorResponse.getMessage());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
