package org.example.client

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import org.example.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserServiceClient {

    @GET("/api/users/{userEmail}")
    suspend fun getUserByEmail(
        @Path("userEmail") userEmail: String
    ): UserDto

    @POST("/api/users/verify/otp")
    suspend fun updateUserOtpStatus(
        @Query("email") userEmail: String,
        @Query("newPhoneNumber") newPhoneNumber: String
    ): Unit
}
