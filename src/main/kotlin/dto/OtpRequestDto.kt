package org.example.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class OtpRequestDto(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
    val phoneNumber: String,

    @field:NotBlank(message = "OTP is required")
    val otp: String? = null
)
