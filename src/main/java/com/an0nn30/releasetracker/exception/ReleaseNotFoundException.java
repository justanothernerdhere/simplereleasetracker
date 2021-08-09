package com.an0nn30.releasetracker.exception;


/***
 * ReleaseNotFoundException.java
 *
 * Custom exception to handle when release is not found during search
 */

public class ReleaseNotFoundException extends RuntimeException {
        public ReleaseNotFoundException() {
            super("No releases found for specific id");
        }
}
