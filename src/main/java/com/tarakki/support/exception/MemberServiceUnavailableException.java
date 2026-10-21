package com.tarakki.support.exception;

public class MemberServiceUnavailableException extends RuntimeException {

    public MemberServiceUnavailableException(Throwable cause) {
        super("Member service is currently unavailable", cause);
    }
}