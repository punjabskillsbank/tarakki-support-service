package com.tarakki.support.exception;

public class SupportTicketMessageNotFoundException extends RuntimeException {

    public SupportTicketMessageNotFoundException(Object id) {
        super("Support ticket not found with ID: " + id);
    }
}
