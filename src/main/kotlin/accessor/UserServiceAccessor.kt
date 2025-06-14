package org.example.accessor

import com.github.michaelbull.result.*
import org.example.client.UserServiceClient
import org.example.dto.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserAccessor(
    @Value("\${USER_SERVICE_BASE_URL}") private val baseUrl: String,  // Base URL from environment
    @Value("\${USER_SERVICE_USERNAME}") private val username: String, // Username from secrets
    @Value("\${USER_SERVICE_PASSWORD}") private val password: String  // Password from secrets
) {

    private val genericAccessor = GenericAccessor(
        UserServiceClient::class.java,
        baseUrl,
        username,
        password
    )

    private val userServiceClient: UserServiceClient by lazy { genericAccessor.service }

    suspend fun getUserByEmail(userEmail: String): Result<UserDto, Error> {
            return userServiceClient
                .runCatching { getUserByEmail(userEmail) }
                .mapError { Error(it) }
    }

    suspend fun updateUserOtpStatus(userEmail: String, newPhoneNumber: String): Result<Unit, Error> {
            return userServiceClient
                .runCatching { updateUserOtpStatus(userEmail, newPhoneNumber) }
                .mapError { Error(it) }
    }
}
