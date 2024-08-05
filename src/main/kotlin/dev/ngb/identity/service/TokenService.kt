package dev.ngb.identity.service

import dev.ngb.identity.config.property.TokenProperties
import dev.ngb.identity.constant.CommonConstant
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

interface TokenService {
    fun generateToken(userId: String, type: TokenType): String
    fun getUserIdFromToken(token: String, type: TokenType): String?
    fun revokeToken(token: String, type: TokenType)
    fun revokeUserTokens(userId: String)
    fun revokeExpiredUserTokens()
    fun isUserHasToken(userId: String, type: TokenType): Boolean
}

enum class TokenType {
    REFRESH, VERIFICATION
}

@Service
class TokenServiceImpl(
    private val redis: RedisTemplate<String, String>,
    private val properties: TokenProperties
) : TokenService {

    override fun generateToken(userId: String, type: TokenType): String {
        val token = generateToken()
        saveUserToken(token, type, userId)
        return token
    }

    override fun getUserIdFromToken(token: String, type: TokenType): String? {
        val tokenKey = "${type.name}-$token"
        return redis.opsForValue().get(tokenKey)
    }

    override fun revokeExpiredUserTokens() {
        val userIds = redis.keys("${CommonConstant.USER_TOKEN_PREFIX}*").map { it.substringAfter('-') }
        userIds.forEach { userId ->
            val tokens = redis.opsForList().range(userId, 0, -1) ?: emptyList()
            tokens.forEach {
                redis.opsForValue().get(it) ?: redis.opsForList().remove(userId, 1, it)
            }
        }
    }

    override fun isUserHasToken(userId: String, type: TokenType): Boolean {
        val userIdKey = "${CommonConstant.USER_TOKEN_PREFIX}$userId"
        val tokens = redis.opsForList().range(userIdKey, 0, -1) ?: emptyList()
        return tokens.filter { it.startsWith(type.name) }.any { redis.opsForValue().get(it) != null }
    }

//    override fun revokeExpiredUserTokens() {
//        // Scan for keys to avoid performance issues with KEYS command
//        val scan = ScanOptions.scanOptions().match("USER-*").count(1000).build()
//        val cursor = redis.scan(scan)
//
//        cursor.use {
//            while (cursor.hasNext()) {
//                val key = cursor.next()
//                val userId = key.substringAfter('-')
//                val tokens = redis.opsForList().range(userId, 0, -1) ?: emptyList()
//
//                if (tokens.isNotEmpty()) {
//                    // Using pipeline to perform batch operations
//                    redis.executePipelined {
//                        val redisOperations = it as RedisOperations<String, *>
//                        tokens.forEach { token ->
//                            val value = redisOperations.opsForValue().get(token)
//                            if (value == null) {
//                                redisOperations.opsForList().remove(userId, 1, token)
//                            }
//                        }
//                        null
//                    }
//                }
//            }
//        }
//    }

    override fun revokeToken(token: String, type: TokenType) {
        val tokenKey = "${type.name}-$token"
        val userId = redis.opsForValue().get(tokenKey) ?: throw IllegalArgumentException("Invalid token")
        redis.delete(tokenKey)
        val userIdKey = "${CommonConstant.USER_TOKEN_PREFIX}$userId"
        redis.opsForList().remove(userIdKey, 1, tokenKey)
    }


    override fun revokeUserTokens(userId: String) {
        val userIdKey = "${CommonConstant.USER_TOKEN_PREFIX}$userId"
        val tokens = redis.opsForList().range(userIdKey, 0, -1) ?: emptyList()
        tokens.forEach { redis.delete(it) }
        redis.delete(userIdKey)
    }

    private fun saveUserToken(token: String, type: TokenType, userId: String) {
        val duration = getTokenDuration(type)
        val tokenKey = "${type.name}-$token"
        redis.opsForValue().set(tokenKey, userId, duration, TimeUnit.MILLISECONDS)
        val userIdKey = "${CommonConstant.USER_TOKEN_PREFIX}$userId"
        redis.opsForList().leftPush(userIdKey, tokenKey)
    }

    private fun generateToken(): String {
        return UUID.randomUUID().toString()
    }

    private fun getTokenDuration(type: TokenType): Long {
        return when (type) {
            TokenType.REFRESH -> properties.refreshTokenDuration
            TokenType.VERIFICATION -> properties.verificationTokenDuration
        }
    }
}