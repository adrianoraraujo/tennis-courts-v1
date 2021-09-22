package com.tenniscourts.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Already exists entity exception.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlreadyExistsEntityException extends RuntimeException {
    /**
     * Instantiates a new Already exists entity exception.
     *
     * @param msg the msg
     */
    public AlreadyExistsEntityException(String msg) {
        super(msg);
    }
}
