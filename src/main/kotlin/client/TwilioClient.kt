package org.example.client

import org.example.dto.TwilioResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TwilioClient {
    @FormUrlEncoded
    @POST("/Accounts/{accountSid}/Messages.json")
    fun sendOtp(
        @Field("To") to: String,
        @Field("MessagingServiceSid") messagingServiceSid: String,
        @Field("Body") body: String
    ): Call<TwilioResponse>
}