package dev.ngb.identity.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "\${springdoc.openapi.title}",
        version = "\${springdoc.openapi.version}",
        contact = Contact(
            name = "\${springdoc.openapi.contact.name}",
            email = "\${springdoc.openapi.contact.email}",
            url = "\${springdoc.openapi.contact.url}"
        ),
        summary = "\${springdoc.openapi.summary}",
        description = "\${springdoc.openapi.description}",
        termsOfService = "\${springdoc.openapi.termsOfService}"
    )
)
class OpenAPIConfig {
    @Bean
    fun customOpenAPIConfiguration(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        return OpenAPI().apply {
            addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            components = Components().addSecuritySchemes(securitySchemeName, SecurityScheme().apply {
                name = securitySchemeName
                type = SecurityScheme.Type.HTTP
                scheme = "bearer"
                bearerFormat = "JWT"
            })
        }
    }

    @Bean
    fun modelResolver(objectMapper: ObjectMapper): ModelResolver {
        return ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE))
    }
}