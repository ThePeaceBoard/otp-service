package org.example.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)  // This will ignore any fields not present in the data class
data class UserDto(
    val id: Long,
    val email: String,
    val phoneNumber: String,
)
