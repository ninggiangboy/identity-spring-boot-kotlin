package dev.ngb.identity.config.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "springdoc.openapi")
data class OpenAPIProperties(
    var title: String,
    var version: String,
    var contact: Contact,
    var summary: String,
    var description: String,
    var termsOfService: String
) {
    data class Contact(
        var name: String,
        var email: String,
        var url: String
    )
}
