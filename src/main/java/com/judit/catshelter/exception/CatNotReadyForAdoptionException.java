package com.judit.catshelter.exception;

public class CatNotReadyForAdoptionException extends RuntimeException {
    public CatNotReadyForAdoptionException(String name) {
        super(name + " is not ready for adoption.");
    }
}
