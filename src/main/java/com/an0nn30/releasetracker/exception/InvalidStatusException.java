package com.an0nn30.releasetracker.exception;

/***
 * InvalidStatusException.java
 *
 * Custom exception to handle client trying to pass invalid status
 */

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super("Invalid status");
    }
}
