package org.example.dto

data class TwilioResponse(
    val sid: String,
    val status: String,
    val errorCode: String?,
    val errorMessage: String?
)

