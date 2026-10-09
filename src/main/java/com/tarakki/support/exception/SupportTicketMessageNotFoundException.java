package com.tarakki.support.exception;

public class SupportTicketMessageNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Support ticket not found with ID: ";

    public SupportTicketMessageNotFoundException(Object id) {
        super(DEFAULT_MESSAGE + id);
    }

    public SupportTicketMessageNotFoundException(String message) {
        super(message);
    }
}
