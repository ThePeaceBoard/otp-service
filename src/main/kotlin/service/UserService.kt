package org.example.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.example.accessor.UserAccessor
import org.example.dto.UserDto
import org.example.error.ServiceError
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userAccessor: UserAccessor
) {
    suspend fun verifyUserExists(userEmail: String): Result<UserDto, ServiceError> {
        return userAccessor.getUserByEmail(userEmail)
            .mapError { ServiceError("Failed getting user by email", it) }
    }

    suspend fun updateUserAfterOtpVerification(userEmail: String, newPhoneNumber: String): Result<Unit, Throwable> {
        return userAccessor.updateUserOtpStatus(userEmail, newPhoneNumber)
    }
}
