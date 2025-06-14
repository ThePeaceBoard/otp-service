package org.example.service

import com.github.michaelbull.result.Result
import kotlinx.coroutines.reactor.mono
import org.example.accessor.TwilioAccessor
import org.example.dto.TwilioResponse
import org.example.error.ServiceError
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TwilioService(
    private val twilioAccessor: TwilioAccessor
) {

    suspend fun sendOtp(to: String, otp: String): Result<String, ServiceError> {
        return twilioAccessor.sendOtp(to, otp)
    }
}

