package com.lunghr.informationsystemslab1.auth.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Suppress("DEPRECATION")
@Schema(description = "Sign in request DTO")
class AuthRequest(

    @Schema(description = "Username", example = "Oleg")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    @NotBlank(message = "Username is required")
    var username: String,

    @Schema(description = "Password", example = "123456")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password is required")
    var password: String
)
