package dev.ngb.identity.service

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.ngb.identity.config.property.JwtProperties
import dev.ngb.identity.constant.CommonConstant
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


interface JwtService {
    fun generateAccessToken(user: UserDetails): String
}

@Service
class JwtServiceImpl(private val properties: JwtProperties) : JwtService {
    override fun generateAccessToken(user: UserDetails): String {
        val currentTime = System.currentTimeMillis()
        return JWTClaimsSet.Builder()
            .subject(user.username)
            .issueTime(Date(currentTime))
            .expirationTime(Date(currentTime + properties.expirationDuration))
            .claim(CommonConstant.ROLES_CLAIM, getRolesClaims(user))
            .claim(CommonConstant.PERMISSIONS_CLAIM, getPermissionsClaims(user))
            .build()
            .let { SignedJWT(JWSHeader(getAlgorithm()), it) }
            .apply { sign(getSignerKey()) }
            .serialize()
    }

    private fun getSignerKey(): JWSSigner {
        return MACSigner(properties.secretKey.toByteArray())
    }

    private fun getAlgorithm(): JWSAlgorithm {
        return JWSAlgorithm.RS256
    }

    private fun getPermissionsClaims(user: UserDetails): List<String> {
        return user.authorities
            .filter { !it.authority.startsWith(CommonConstant.ROLE_PREFIX) }
            .map { it.authority }
    }

    private fun getRolesClaims(user: UserDetails): List<String> {
        return user.authorities
            .filter { it.authority.startsWith(CommonConstant.ROLE_PREFIX) }
            .map { it.authority }
    }
}