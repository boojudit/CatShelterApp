package com.judit.catshelter.exception;

import com.judit.catshelter.model.CatInteractionType;

public class CooldownException extends RuntimeException {
    public CooldownException(CatInteractionType type) {
        super(
                type + " is on cooldown. Try again later."
        );
    }
}
