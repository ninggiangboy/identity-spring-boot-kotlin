package dev.ngb.identity.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token.jwt")
data class JwtProperties(
    val secretKey: String,
    val expirationDuration: Long
)