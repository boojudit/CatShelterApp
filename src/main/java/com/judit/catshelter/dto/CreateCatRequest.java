package com.judit.catshelter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCatRequest(
        @NotBlank(message = "Cat name is required")
        @Size(max=50, message = "Name must be under 50 characters")
        String name
) {
}
