package dev.ngb.identity.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "link")
data class LinkProperties(
    val verifyEmail: String,
    val resetPassword: String
)