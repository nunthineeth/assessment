package com.kbtg.bootcamp.posttest.utils;

public enum ErrorMessage {
    VALIDATION_FAILED("Validation Failed"),
    RESOURCE_NOT_FOUND("Resource not found"),
    USERID_VALIDATE_LENGTH_MSG("UserId length should be 10 characters"),
    TICKET_ID_VALIDATE_LENGTH_MSG("TicketId length should be 6 characters"),
    NO_RESOURCE_FOUND_TO_SELL_BACK("No resource found to sell back"),
    TICKET_IS_REQUIRED("Ticket is required"),
    PRICE_IS_REQUIRED("Price is required"),
    AMOUNT_IS_REQUIRED("Amount is required"),
    INVALID_TICKET_ID("Invalid tickets id"),
    INVALID_PRICE_VALUE("Invalid price value"),
    INVALID_AMOUNT_VALUE("Invalid amount value"),
    ERROR_OCCURRED_BUY_LOTTERY("Error occurred while buying lottery tickets"),
    ERROR_TICKET_ALREADY_EXIST("Ticket already exist"),
    ERROR_TICKET_NOT_FOUND("Lottery Tickets not found"),
    TICKETS_HAVE_BEEN_SOLD_OUT("Lottery tickets have been sold out");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
