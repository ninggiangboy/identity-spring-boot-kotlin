package dev.ngb.identity.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token")
data class TokenProperties(
    val refreshTokenDuration: Long,
    val verificationTokenDuration: Long
)