package dev.ngb.identity.config.security

import dev.ngb.chatapp.app.identity.domain.entity.AppUser
import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import dev.ngb.identity.constant.CommonConstant
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val authenticationEntryPoint: AuthenticationEntryPoint,
) {
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.anyRequest().permitAll() // Permit all requests
        }
        http.oauth2ResourceServer {
            it.jwt { jwt ->
                jwt.jwtAuthenticationConverter(
                    jwtConverter()
                )
            }.authenticationEntryPoint(authenticationEntryPoint)
        }
        http.csrf { it.disable() } // Disable CSRF
        http.cors { it.disable() } // Disable CORS
        return http.build()
    }

    fun jwtConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter(CustomJwtGrantedAuthoritiesConverter())
        return converter
    }

    private class CustomJwtGrantedAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {
        override fun convert(jwt: Jwt): List<GrantedAuthority> {
            val roles = jwt.getClaimAsStringList(CommonConstant.ROLES_CLAIM)?.map { SimpleGrantedAuthority(it) } ?: emptyList()
            val permissions = jwt.getClaimAsStringList(CommonConstant.PERMISSIONS_CLAIM)?.map { SimpleGrantedAuthority(it) } ?: emptyList()
            return roles + permissions
        }
    }

    @Bean
    fun userDetailsService(appUserRepository: AppUserRepository): UserDetailsService {
        return UserDetailsService { username: String ->
            appUserRepository.findByUsernameOrEmail(username, username)?.let {
                User.builder()
                    .username(it.id.toString())
                    .password(it.hashPassword ?: "")
                    .disabled(!(it.isEnabled ?: false))
                    .accountLocked(it.isLooked ?: false)
                    .authorities(it.grantedAuthorities())
                    .build()
            } ?: throw UsernameNotFoundException("User $username not found")
        }
    }

    private fun AppUser.grantedAuthorities(): List<GrantedAuthority> {
        val roles = this.roles.map { SimpleGrantedAuthority(it.name) }
        val permissions = this.roles.flatMap { it.permissions }.map { SimpleGrantedAuthority(it.name) }
        return roles + permissions
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}