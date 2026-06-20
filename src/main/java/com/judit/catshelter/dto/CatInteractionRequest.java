package com.judit.catshelter.dto;

import com.judit.catshelter.model.CatInteractionType;
import jakarta.validation.constraints.NotNull;

public record CatInteractionRequest(
        @NotNull(message = "Interaction type is required")
        CatInteractionType type
) {
}
