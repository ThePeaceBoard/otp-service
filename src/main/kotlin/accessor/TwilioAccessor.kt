package org.example.accessor

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import org.example.client.TwilioClient
import org.example.dto.TwilioResponse
import org.example.error.ServiceError
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import retrofit2.await

@Component
class TwilioAccessor(
    @Value("\${TWILIO_BASE_URL}") private val baseUrl: String,  // Base URL from environment
    @Value("\${TWILIO_ACCOUNT_SID}") private val accountSid: String,
    @Value("\${TWILIO_AUTH_TOKEN}") private val authToken: String,  // Auth Token from secrets
    @Value("\${TWILIO_MESSAGING_SERVICE_SID}") private val messagingServiceSid: String // Messaging service ID
) {
    @PostConstruct
    fun init() {
        // Initialize Twilio with account SID and Auth Token
        Twilio.init(accountSid, authToken)
    }

    private val retrofitAccessor = GenericAccessor(
        TwilioClient::class.java,
        baseUrl,
        accountSid,
        authToken
    )

    private val twilioClient: TwilioClient by lazy { retrofitAccessor.service }

    // Function to send OTP using Twilio SDK
    suspend fun sendOtp(to: String, otp: String): Result<String, ServiceError> {
        return try {
            val otpMessage = """
                Your verification code is: $otp
            """.trimIndent()

            // Using Twilio SDK to send the OTP message
            val message = Message.creator(
                PhoneNumber(to),
                messagingServiceSid,
                otpMessage
            ).create()

            Ok(message.sid)  // Return message SID upon success
        } catch (e: Exception) {
            Err(ServiceError("Failed to send OTP: ${e.message}", e))
        }
    }
}
