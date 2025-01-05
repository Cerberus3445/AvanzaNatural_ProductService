package com.cerberus.product_service.exception;

public class NotFoundException extends RuntimeException {

    private final static String MESSAGE = "%s with %d id not found.";

    public NotFoundException(String object, Integer id) {
        super(MESSAGE.formatted(object, id));
    }
}
