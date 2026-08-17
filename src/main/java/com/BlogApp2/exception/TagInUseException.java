package com.BlogApp2.exception;

import java.util.UUID;

public class TagInUseException extends RuntimeException {
    public TagInUseException(UUID id) {
        super("Tag " + id + " cannot be deleted because it is still attached to one or more posts");
    }
}
