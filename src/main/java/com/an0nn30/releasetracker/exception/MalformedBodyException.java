package com.an0nn30.releasetracker.exception;

/***
 * MalformedBodyException.java
 *
 * Custom exception to handle client trying to pass imcomplete/malformed body
 */
public class MalformedBodyException extends RuntimeException{
    public MalformedBodyException() {
        super("Request body is malformed");
    }
}
