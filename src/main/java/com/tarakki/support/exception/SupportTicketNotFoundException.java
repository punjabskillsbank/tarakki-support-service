package com.tarakki.support.exception;

public class SupportTicketNotFoundException extends RuntimeException {

    public SupportTicketNotFoundException(Object id) {
        super("Support ticket not found with ID: " + id);
    }
}
