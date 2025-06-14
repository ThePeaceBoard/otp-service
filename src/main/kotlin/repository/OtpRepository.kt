package org.example.repository

import org.example.entity.OtpEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface OtpRepository : R2dbcRepository<OtpEntity, Long> {
    @Query("SELECT * FROM OTP WHERE user_id = :userId ORDER BY expiration_time DESC LIMIT 1")
    fun getByUserId(userId: String): Mono<OtpEntity?>

    @Query("DELETE FROM OTP WHERE user_id = :userId")
    fun deleteByUserId(userId: String): Mono<Unit>
}
