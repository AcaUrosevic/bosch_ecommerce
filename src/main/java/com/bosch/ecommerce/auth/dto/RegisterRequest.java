package com.bosch.ecommerce.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotBlank String email,
        @Size(min=6, message="Password min 6 chars") String password
) {}
