package org.example.accessor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class GenericAccessor<T>(
    private val serviceClass: Class<T>,
    private val baseUrl: String,
    private val username: String,
    private val password: String,
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(basicAuthInterceptor(username, password))
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }

    val service: T by lazy {
        retrofit.create(serviceClass)
    }

    private fun basicAuthInterceptor(username: String, password: String): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build()
            chain.proceed(request)
        }
    }
}
