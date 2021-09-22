package com.tenniscourts.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The type Business exception.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {
    /**
     * Instantiates a new Business exception.
     *
     * @param msg the msg
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
