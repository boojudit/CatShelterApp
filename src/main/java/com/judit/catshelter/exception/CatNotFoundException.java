package com.judit.catshelter.exception;

public class CatNotFoundException extends RuntimeException {
    public CatNotFoundException(Long id) {
        super("Could not find cat with id " + id);
    }
}
