package org.example.service

import com.github.michaelbull.result.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.example.entity.OtpEntity
import org.example.error.ServiceError
import org.example.repository.OtpRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OtpService(
    private val otpRepository: OtpRepository,
    private val userService: UserService,
    private val twilioService: TwilioService
) {
    suspend fun generateOtp(userEmail: String, phone: String): Result<String, ServiceError> {
        return userService.verifyUserExists(userEmail)
            .map {
                otpRepository.save(
                    OtpEntity(
                        userId = it.id.toString(),
                        otpCode = (100000..999999).random().toString(),
                        expirationTime = LocalDateTime.now().plusMinutes(5)
                    )
                )
            }.andThen {
                twilioService.sendOtp(phone, it.awaitSingle().otpCode)
            }
            .mapError {
                ServiceError("Couldn't generate OTP", it.throwable)
            }
    }

    suspend fun validateOtp(userEmail: String, otpCode: String, phoneNumber: String): Result<Boolean, ServiceError> {
        return userService.verifyUserExists(userEmail)
            .andThen { userDto ->
                otpRepository.getByUserId(userDto.id.toString()).awaitSingleOrNull()?.let { otpEntity ->
                    when {
                        otpEntity.expirationTime.isBefore(LocalDateTime.now()) -> {
                            otpRepository.deleteByUserId(otpEntity.userId).awaitSingle()
                            Err(ServiceError("OTP has expired"))
                        }
                        otpEntity.otpCode != otpCode -> Err(ServiceError("Invalid OTP"))
                        else -> try {
                            // Try to update the user and delete OTP
                            userService.updateUserAfterOtpVerification(userEmail, phoneNumber)
                            otpRepository.deleteByUserId(otpEntity.userId).awaitSingle()
                            Ok(true)
                        } catch (e: Exception) {
                            // Catching any exceptions from updateUserAfterOtpVerification or delete
                            Err(ServiceError("Failed to verify OTP and update user: ${e.message}", e))
                        }
                    }
                } ?: Err(ServiceError("OTP not found for the user"))
            }
            .mapError { ServiceError("An error occurred during OTP verification", it.throwable) }
    }
}
