package com.judit.catshelter.exception;

public class CatAlreadyAdoptedException extends RuntimeException {
    public CatAlreadyAdoptedException(String name) {
        super(name + " is already adopted.");
    }
}
