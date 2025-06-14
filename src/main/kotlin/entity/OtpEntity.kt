package org.example.entity

import jakarta.validation.constraints.Email
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


@Table(name = "OTP")
data class OtpEntity(
    @Id
    val id: Long = 0,

    @Column("user_id")
    val userId: String,

    @Column("otp_code")
    val otpCode: String,

    @Column("expiration_time")
    val expirationTime: LocalDateTime
)

