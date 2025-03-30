package com.cerberus.product_service.exception;

public class NotFoundException extends RuntimeException {

    private final static String MESSAGE = "%s with %d id not found.";

    private final static String EMAIL_MASSAGE = "User with this email not found";

    public NotFoundException(String object, Integer id) {
        super(MESSAGE.formatted(object, id));
    }

    public NotFoundException() {
        super(EMAIL_MASSAGE);
    }
}
