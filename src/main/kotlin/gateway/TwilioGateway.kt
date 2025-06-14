package org.example.gateway

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import org.example.error.ServiceError
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TwilioGateway(
    @Value("\${TWILIO_ACCOUNT_SID}") private val accountSid: String,
    @Value("\${TWILIO_AUTH_TOKEN}") private val authToken: String,
    @Value("\${TWILIO_MESSAGING_SERVICE_SID}") private val messagingServiceSid: String
) {

    fun sendOtp(to: String, otp: String): Result<String, ServiceError> {
        return try {
            val otpMessage = """
            Your verification code is: $otp
            
            $otp
        """.trimIndent()

            // Send the OTP message using the Messaging Service SID
            val message = Message.creator(
                PhoneNumber(to),
                messagingServiceSid,
                otpMessage
            ).create()
            return Ok(message.sid)

        } catch (e: Exception) {
            Err(ServiceError("Failed to send OTP: ${e.message}", e))
        }
    }
}