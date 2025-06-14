package org.example.controller

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.map
import org.example.service.OtpService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/otp")
class OTPController(private val otpService: OtpService) {

    @PostMapping("/generate")
    suspend fun generateOtp(
        @RequestParam email: String,
        @RequestParam phone: String
    ): ResponseEntity<String> {
        return otpService.generateOtp(email, phone)
            .fold (
                {ResponseEntity.ok("OTP sent successfully")},
                {ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate OTP")}
            )

    }

    @PostMapping("/validate")
    suspend fun validateOtp(
        @RequestParam email: String,
        @RequestParam otpCode: String,
        @RequestParam phoneNumber: String
    ): ResponseEntity<String> {
        return otpService.validateOtp(email, otpCode, phoneNumber)
            .fold(
                {ResponseEntity.ok("OTP validated successfully")},
                {it.toResponseEntity()}//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to validate OTP")
            )
    }
}
