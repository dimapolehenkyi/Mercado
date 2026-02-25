package com.example.mercado.exception.exceptions.mailException;


import lombok.Getter;

public class EmailSendFailedException extends RuntimeException {

    @Getter
    private final String email;

    @Getter
    private final String code = "FAILED_TO_SEND_EMAIL";

    public EmailSendFailedException(String email, Throwable cause) {
        super("Failed to send email to " + email, cause);
        this.email = email;
    }
}
