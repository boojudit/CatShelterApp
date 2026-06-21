package com.judit.catshelter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCatRequest(
        @NotBlank(message = "Cat name is required")
        @Size(max=30, message = "Name must be under 30 characters")
        String name
) {
}
