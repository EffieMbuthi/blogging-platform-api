package com.BlogApp2.exception;

public class DuplicateTagException extends RuntimeException {
    public DuplicateTagException(String name) {
        super("A tag named '" + name + "' already exists");
    }
}
