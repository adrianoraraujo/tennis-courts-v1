package com.tenniscourts.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Entity not found exception.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Entity not found exception.
     *
     * @param msg the msg
     */
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
