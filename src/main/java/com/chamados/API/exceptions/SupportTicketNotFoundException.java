package com.chamados.API.exceptions;

public class SupportTicketNotFoundException extends RuntimeException {
    public SupportTicketNotFoundException(String message) {
        super(message);
    }
}
