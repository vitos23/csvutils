package com.vitos23.csvutils.exceptions;

public class IncorrectInputException extends ParseException {
    public IncorrectInputException() {
    }

    public IncorrectInputException(String message) {
        super(message);
    }
}
