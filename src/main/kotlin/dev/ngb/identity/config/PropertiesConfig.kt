package dev.ngb.identity.config

import dev.ngb.identity.config.property.JwtProperties
import dev.ngb.identity.config.property.LinkProperties
import dev.ngb.identity.config.property.OpenAPIProperties
import dev.ngb.identity.config.property.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        OpenAPIProperties::class,
        JwtProperties::class,
        TokenProperties::class,
        LinkProperties::class
    ]
)
class PropertiesConfig