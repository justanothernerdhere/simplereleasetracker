package com.an0nn30.releasetracker.exception;


/***
 * MissingIDException.java
 *
 * Custom exception to handle missing ID during update api call.
 */

public class MissingIDException extends RuntimeException {
    public MissingIDException() {
        super("Body missing release id");
    }
}
