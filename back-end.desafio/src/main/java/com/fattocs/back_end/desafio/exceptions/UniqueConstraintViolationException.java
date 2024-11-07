package com.fattocs.back_end.desafio.exceptions;

import java.io.Serial;

public class UniqueConstraintViolationException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public UniqueConstraintViolationException(String message) {
        super(message);

    }
}
